package com.workshop.sudokubook.dao

import com.wework.jpa.JpaBean
import com.workshop.sudokubook.DbTestExtension
import com.workshop.sudokubook.TestConfig
import com.workshop.sudokubook.entity.EntityDao
import com.workshop.sudokubook.entity.TestEntity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.test.context.ContextConfiguration

@ExtendWith(DbTestExtension::class)
@ContextConfiguration(classes = [TestConfig::class, TestDao::class])
internal class EntityDaoTest {

    @Autowired
    private lateinit var jpaBean: JpaBean

    @Autowired
    private lateinit var testDao: TestDao

    @Test
    fun findAll() {
        testDao.save(TestEntity().apply {
            tag = "one"
            deleted = false
        })
        testDao.save(TestEntity().apply {
            tag = "two"
            deleted = true
        })

        // method under test
        val results = testDao.findAll()

        // assertions
        val retOne = results.find { it.tag == "one" && !it.deleted && it.id != null }
        assertNotNull(retOne)
        val retTwo = results.find { it.tag == "two" && it.deleted && it.id != null }
        assertNotNull(retTwo)
    }

    @Nested
    @DisplayName("findById")
    internal inner class FindById {
        @Test
        fun `entity not found`() {
            val entity = testDao.findById(0L)
            assertNull(entity)
        }

        @Test
        fun `entity found`() {
            val entity = TestEntity()
            testDao.save(entity)

            // check only 1 record exists
            val allEntities = testDao.findAll()
            assertEquals(1, allEntities.size)

            // method under test
            val result = testDao.findById(entity.id!!)

            // assertions
            assertEquals(entity.id, result?.id)
        }
    }

    @Nested
    @DisplayName("findByIds")
    internal inner class FindByIds {
        @Test
        fun `empty list`() {
            val records = testDao.findByIds(emptyList())
            assertTrue(records.isEmpty())
        }

        @Test
        fun `retrieving records by specified ids should return those records`() {
            val one = testDao.save(TestEntity().apply { tag = "one" })
            val two = testDao.save(TestEntity().apply { tag = "two" })
            testDao.save(TestEntity().apply { tag = "three" })

            // method under test
            val results = testDao.findByIds(listOf(one.id!!, two.id!!))

            // assertions
            assertEquals(2, results.size)
            val retOne = results.find { it.tag == "one" && it.id == one.id }
            assertNotNull(retOne)
            val retTwo = results.find { it.tag == "two" && it.id == two.id }
            assertNotNull(retTwo)
        }
    }

    @Nested
    @DisplayName("save")
    internal inner class Save {
        @Test
        fun `PERSISTENT state - entity is managed by the EntityManager`() {
            // changes need to reflect in DB at the end of transaction
            val managed = jpaBean.withTx { em ->
                // create a managed entity
                val entity = TestEntity().apply { tag = "old tag" }
                em.persist(entity)

                // make a change
                entity.tag = "new tag"

                // method under test
                testDao.save(entity)
            }

            // check that the object has changed in the DB
            val result = testDao.findById(managed!!.id!!)
            assertEquals("new tag", result?.tag)
        }

        @Test
        fun `TRANSIENT state - entity is new and un-managed`() {
            assertTrue(testDao.findAll().isEmpty())

            // method under test
            val saved = testDao.save(TestEntity())

            // check only 1 record exists
            val allEntities = testDao.findAll()
            assertEquals(1, allEntities.size)
            val record = allEntities.first()

            // assertions
            assertEquals(record.id, saved.id)
        }

        @Test
        fun `DETACHED or DELETED state`() {
            val entity = jpaBean.withTx {
                testDao.save(TestEntity().apply { tag = "old tag" })
            }
            val existing = testDao.findAll()
            assertEquals(1, existing.size)
            assertEquals("old tag", existing.first().tag)

            jpaBean.withTx { em ->
                // detach the entity
                em.detach(entity!!)

                // check there is 1 record in DB
                assertEquals(1, testDao.findAll().size)

                // make a change
                entity.tag = "new tag"

                // method under test
                testDao.save(entity)
            }

            // check that there is still 1 record in DB, i.e. the entity was merged
            val records = testDao.findAll()
            assertEquals(1, records.size)
            assertEquals("new tag", records.first().tag)
        }
    }

    @Nested
    @DisplayName("delete")
    internal inner class Delete {
        @Test
        fun `delete a managed entity`() {
            jpaBean.withTx { em ->
                val entity = TestEntity()
                em.persist(entity)

                // check entity is stored
                val stored = testDao.findAll()
                assertEquals(1, stored.size)
                assertEquals(entity.id!!, stored.first().id)

                // method under test
                testDao.delete(entity)
            }

            // assertions
            assertTrue(testDao.findAll().isEmpty())
        }

        @Test
        fun `delete a detached entity`() {
            val entity = testDao.save(TestEntity())
            assertEquals(1, testDao.findAll().size)

            jpaBean.withTx { em ->
                em.detach(entity)

                // method under test
                testDao.delete(entity)
            }

            // assertions
            assertTrue(testDao.findAll().isEmpty())
        }
    }

    @Nested
    @DisplayName("deleteById")
    internal inner class DeleteById {
        @Test
        fun `delete non-existing entity`() {
            val one = testDao.save(TestEntity().apply {
                tag = "one"
                deleted = false
            })

            // check there is one entity stored
            assertEquals(1, testDao.findAll().size)

            // delete entity that doesn't exist
            testDao.deleteById(one.id!! - 1)

            // check the original entity is still there
            assertEquals(1, testDao.findAll().size)
        }

        @Test
        fun `delete existing entity`() {
            val entity = testDao.save(TestEntity())

            // check only 1 record exists
            assertEquals(1, testDao.findAll().size)

            // method under test
            testDao.deleteById(entity.id!!)

            // assertions
            assertTrue(testDao.findAll().isEmpty())
        }
    }
}

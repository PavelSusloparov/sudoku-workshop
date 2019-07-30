package com.workshop.sudokubook.entity

import java.time.LocalDateTime
import java.time.ZoneId
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.transaction.Transactional
import kotlin.reflect.KClass

abstract class EntityDao<E : BaseEntity>(var clazz: KClass<E>) {

    @PersistenceContext
    lateinit var em: EntityManager

    /**
     * Search methods
     */

    @Transactional
    open fun find(entity: E): E {
        return em.find(entity::class.java, entity.id)
    }

    /**
     * Fetches all entities, including soft-deleted ones
     */
    @Transactional
    open fun findAll(): List<E> {
        val query = em.criteriaBuilder.createQuery(clazz.java)
        return em.createQuery(
            query.select(
                query.from(clazz.java)
            )
        ).resultList
    }

    /**
     * Fetches an entity by id
     */
    @Transactional
    open fun findById(id: Long): E? {
        return em.find(clazz.java, id)
    }

    /**
     * Fetches all entities by the specified ids
     */
    @Transactional
    open fun findByIds(ids: Collection<Long>): List<E> {
        if (ids.isEmpty()) {
            return emptyList()
        }
        val cb = em.criteriaBuilder

        // create select query
        val query = cb.createQuery<E>(clazz.java)
        val fromEntity = query.from(clazz.java)
        return em.createQuery(
            query.select(fromEntity)
                .where(fromEntity.get<Long>("id").`in`(ids))
        ).resultList
    }

    /**
     * Save/update methods
     */

    /**
     * Handles save or update on the entity according to the 4 Entity states in Hibernate
     * (PERSISTENT, TRANSIENT, DETACHED, DELETED)
     *
     * The following resources were used to design the logic below:
     *  - simple guide to the state transitions with JPA and Hibernate:
     *    https://vladmihalcea.com/a-beginners-guide-to-jpa-hibernate-entity-state-transitions/
     *  - explanation how to distinguish between TRANSIENT, MANAGED, and DETACHED states in code:
     *    https://stackoverflow.com/a/40524287
     *  - rule of thumb when it comes to saving entities:
     *    https://xebia.com/blog/jpa-implementation-patterns-saving-detached-entities/
     */
    @Transactional
    open fun save(entity: E): E {
        // the entity is managed by Hibernate (in PERSISTENT state)
        if (em.contains(entity)) {
            // don't do anything, Hibernate will make any changes
            // at the end of the Session
            entity.updatedOn = LocalDateTime.now(ZoneId.of("UTC")!!)
            return entity
        }

        // the entity is TRANSIENT (new) - not managed by Hibernate and doesn't have an ID
        if (entity.id == null) {
            em.persist(entity)
            return entity
        }

        // the entity is DETACHED or DELETED
        return em.merge(entity)
    }

    /**
     * Deletes an entity
     */
    @Transactional
    open fun delete(entity: E) {
        if (em.contains(entity)) {
            // if it is managed, delete it through the Entity Manager
            em.remove(entity)
        } else {
            // otherwise delete it via a query
            deleteById(entity.id!!)
        }
    }

    /**
     * Deletes the entity if it exists
     */
    @Transactional
    open fun deleteById(id: Long) {
        val cb = em.criteriaBuilder

        // create delete query
        val query = cb.createCriteriaDelete<E>(clazz.java)
        val fromEntity = query.from(clazz.java)
        query.where(cb.equal(fromEntity.get<Long>("id"), id))

        // perform delete
        em.createQuery(query).executeUpdate()
    }
}

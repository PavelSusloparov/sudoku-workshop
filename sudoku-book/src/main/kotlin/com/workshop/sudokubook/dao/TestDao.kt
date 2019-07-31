package com.workshop.sudokubook.dao

import com.workshop.sudokubook.entity.EntityDao
import com.workshop.sudokubook.entity.TestEntity
import org.springframework.stereotype.Component

@Component
class TestDao : EntityDao<TestEntity>(TestEntity::class)

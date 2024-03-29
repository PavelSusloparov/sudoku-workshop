package com.workshop.sudokubook.dao

import com.workshop.sudokubook.entity.EntityDao
import com.workshop.sudokubook.entity.TestEntity
import org.springframework.stereotype.Component

@Component
internal class TestDao : EntityDao<TestEntity>(TestEntity::class)

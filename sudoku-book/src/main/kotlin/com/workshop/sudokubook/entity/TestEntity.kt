package com.workshop.sudokubook.entity

import javax.persistence.Entity

@Entity
class TestEntity(
    var tag: String = "tag",
    var deleted: Boolean = false
) : BaseEntity()

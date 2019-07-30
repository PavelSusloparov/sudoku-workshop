package com.workshop.sudokubook.entity

import java.time.LocalDateTime
import java.time.ZoneId
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class BaseEntity(
    @Id
    @Column(name = "id", columnDefinition = "BIGINT", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // We're using MySQL auto-increment columns.
    var id: Long? = null,

    @Column(name = "created_on", columnDefinition = "TIMESTAMP")
    var createdOn: LocalDateTime = LocalDateTime.now(ZoneId.of("UTC")!!),

    @Column(name = "updated_on", columnDefinition = "TIMESTAMP")
    var updatedOn: LocalDateTime = createdOn
)

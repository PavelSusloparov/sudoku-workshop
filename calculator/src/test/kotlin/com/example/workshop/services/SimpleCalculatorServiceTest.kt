package com.example.workshop.services

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class SimpleCalculatorServiceTest {

    private lateinit var simpleCalculatorService: SimpleCalculatorService

    @BeforeEach
    fun setup() {

        simpleCalculatorService = SimpleCalculatorService()
    }

    @Nested
    @DisplayName("sum")
    internal inner class Sum {

        @Test
        fun `sum Integers`() {
            simpleCalculatorService.sum("1", "2").run {
                assertEquals(3, this)
            }
        }
    }
}

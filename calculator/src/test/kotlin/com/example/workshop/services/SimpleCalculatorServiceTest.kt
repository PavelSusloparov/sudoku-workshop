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
            simpleCalculatorService.sum(1, 2).run {
                assertEquals(3, this)
            }
        }
    }

    @Nested
    @DisplayName("diff")
    internal inner class Diff {

        @Test
        fun `sum Integers`() {
            simpleCalculatorService.diff(2, 1).run {
                assertEquals(1, this)
            }
        }
    }

    @Nested
    @DisplayName("sin")
    internal inner class Sin {

        @Test
        fun `sum Integers`() {
            simpleCalculatorService.sin(0.5).run {
                assertEquals(0.479425538604203, this)
            }
        }
    }
}

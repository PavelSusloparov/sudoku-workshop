package com.workshop.sudokubook.services

import com.workshop.sudokubook.client.SudokuHttpClient
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import com.nhaarman.mockito_kotlin.mock
import com.workshop.sudokubook.dao.SudokuTrackerDao
import org.junit.jupiter.api.Test

class SudokuBookServiceTest {

    private lateinit var sudokuHttpClient: SudokuHttpClient
    private lateinit var sudokuTrackerDao: SudokuTrackerDao

    private lateinit var sudokuBookService: SudokuBookService

    @BeforeEach
    fun setup() {
        sudokuHttpClient = mock()
        sudokuTrackerDao = mock()

        sudokuBookService = SudokuBookService(sudokuHttpClient, sudokuTrackerDao)
    }

    @Nested
    @DisplayName("read")
    internal inner class read {

        @Test
        fun `success`() {
            sudokuBookService.read()
        }
    }
}

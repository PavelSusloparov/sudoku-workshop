package com.workshop.sudokubook.services

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doNothing
import com.nhaarman.mockito_kotlin.doReturn
import com.workshop.sudokubook.client.SudokuHttpClient
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.whenever
import com.workshop.sudokubook.Fixture
import com.workshop.sudokubook.collections.SudokuBookRequest
import com.workshop.sudokubook.collections.SudokuBookResponse
import com.workshop.sudokubook.dao.SudokuTrackerDao
import com.workshop.sudokubook.entity.SudokuTrackerEntity
import org.junit.jupiter.api.Assertions.assertEquals
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
    @DisplayName("create")
    internal inner class Create {

        private lateinit var sudokuBookRequest: SudokuBookRequest
        private lateinit var sudokuBookResponse: SudokuBookResponse

        @BeforeEach
        fun setup() {
            sudokuBookRequest = Fixture.SudokuBook.sudokuBookRequest()

            sudokuBookService = spy(sudokuBookService)
            doReturn(sudokuBookRequest.sudoku).whenever(sudokuBookService).read(any())

            sudokuBookResponse = Fixture.SudokuBook.sudokuBookResponse()
            doReturn(sudokuBookResponse).whenever(sudokuHttpClient).solveSudoku(any())
            doReturn(null).whenever(sudokuTrackerDao).findBySudoku(sudokuBookRequest.sudoku)

            doNothing().whenever(sudokuBookService).saveToFile(any(), any())

            doReturn("").whenever(sudokuBookService).getAbsoluteResultFilePath("")
        }
        @Test
        fun `verify that create is successful if the sudoku book is not empty`() {
            // TODO: Uncomment me.
//            sudokuBookService.create("")
//
//            verify(sudokuBookService, times(1)).solve(sudokuBookRequest.sudoku)
//            val sb = StringBuilder().append(sudokuBookResponse.result).append("\n============\n")
//            verify(sudokuBookService, times(1)).save(sb.toString(), "")
        }
    }

    @Nested
    @DisplayName("transform")
    internal inner class Transform {

        private lateinit var sudokuBookRequest: SudokuBookRequest

        @BeforeEach
        fun setup() {
            sudokuBookRequest = Fixture.SudokuBook.sudokuBookRequest()

            sudokuBookService = spy(sudokuBookService)
            doReturn(sudokuBookRequest.sudoku).whenever(sudokuBookService).read(any())
        }

        @Test
        fun `verify that transform return a list of sudoku if sudoku book is not empty`() {
            sudokuBookService.transform("").run {
                assertEquals(listOf(sudokuBookRequest.sudoku), this)
            }
        }

        @Test
        fun `verify that transform returns empty list of sudoku if sudoku book is empty`() {
            doReturn(null).whenever(sudokuBookService).read(any())

            sudokuBookService.transform("").run {
                assertEquals(emptyList<String>(), this)
            }
        }
    }

    @Nested
    @DisplayName("solve")
    internal inner class Solve {

        private lateinit var sudokuBookRequest: SudokuBookRequest
        private lateinit var sudokuBookResponse: SudokuBookResponse

        @BeforeEach
        fun setup() {
            sudokuBookRequest = Fixture.SudokuBook.sudokuBookRequest()
            sudokuBookResponse = Fixture.SudokuBook.sudokuBookResponse()
            // TODO: Implement me.
        }

        @Test
        fun `verify that exception is handled gracefully if sudoku solve call fails`() {
            // TODO: Implement me.
        }
    }

    @Nested
    @DisplayName("save")
    internal inner class Save {

        @BeforeEach
        fun setup() {
            // TODO: Implement me.
        }

        @Test
        fun `verify that sudoku is not saved if empty`() {
            // TODO: Implement me.
        }
    }

    @Nested
    @DisplayName("saveToDatabase")
    internal inner class SaveToDatabase {

        private lateinit var sudokuBookResponse: SudokuBookResponse
        private lateinit var sudokuTrackerEntity: SudokuTrackerEntity

        @BeforeEach
        fun setup() {
            sudokuBookResponse = Fixture.SudokuBook.sudokuBookResponse()
            sudokuTrackerEntity = Fixture.SudokuBook.sudokuTrackerEntity()
        }

        @Test
        fun `verify that record is updated in database if it is already exist`() {
            // TODO: Implement me.
        }

        @Test
        fun `verify that record is created in database if it is not exist`() {
            // TODO: Implement me.
        }
    }
}

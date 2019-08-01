package com.workshop.sudokubook.stepDefs

import com.workshop.sudokubook.collection.SudokuBookWorld
import com.workshop.sudokubook.dao.SudokuTrackerDao
import com.workshop.sudokubook.mock.SudokuMock
import com.workshop.sudokubook.services.SudokuBookService
import cucumber.api.java.After
import cucumber.api.java.Before
import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import cucumber.api.java.en.When
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull

class SudokuBookComponentStepDefs(
    private val sudokuBookService: SudokuBookService,
    private val sudokuTrackerDao: SudokuTrackerDao,
    private val sudokuMock: SudokuMock
) {

    @Given("sudoku book file with collection of unsolved sudoku")
    fun `sudoku book file with collection of unsolved sudoku`(sudokuBookWorld: SudokuBookWorld) {
        assertNotNull(javaClass.classLoader.getResource(sudokuBookWorld.unsolvedSudokuPath!!)?.readText())
    }

    @Given("solve sudoku call returns same solved sudoku")
    fun `sudoku book file with collection of unsolved sudoku`() {
        sudokuMock.solveSudoku()
    }

    @When("call solve sudoku")
    fun `call solve sudoku`(sudokuBookWorld: SudokuBookWorld) {
        sudokuBookService.create(sudokuBookWorld.unsolvedSudokuPath!!)
    }

    @Then("sudoku book file with collection of solved sudoku is created")
    fun `sudoku book file with collection of solved sudoku is created`(sudokuBookWorld: SudokuBookWorld) {
        assertNotNull(javaClass.classLoader.getResource(sudokuBookWorld.solvedSudokuPath!!.replace("testUtil", "production"))?.readText())
    }

    @Then("each solved sudoku is stored in the database with expected solvedCount")
    fun `each solved sudoku is stored in the database with expected solvedCount`(sudokuBookWorld: SudokuBookWorld) {
        val sudokuTrackers = sudokuTrackerDao.findAll()
        assertEquals(sudokuBookWorld.numberOfDatabaseRecords!!, sudokuTrackers.size)
        sudokuTrackers.forEach {
            assertEquals(sudokuBookWorld.solveCounter!!, it.solveCounter)
        }
    }

}

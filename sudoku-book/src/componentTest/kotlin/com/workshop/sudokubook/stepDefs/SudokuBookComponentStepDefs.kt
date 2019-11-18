package com.workshop.sudokubook.stepDefs

import com.workshop.sudokubook.collection.SudokuBookWorld
import com.workshop.sudokubook.dao.SudokuTrackerDao
import com.workshop.sudokubook.mock.SudokuMock
import com.workshop.sudokubook.services.SudokuBookService
import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import cucumber.api.java.en.When

class SudokuBookComponentStepDefs(
    private val sudokuBookService: SudokuBookService,
    private val sudokuTrackerDao: SudokuTrackerDao,
    private val sudokuMock: SudokuMock
) {

    @Given("sudoku book file with collection of unsolved sudoku")
    fun `sudoku book file with collection of unsolved sudoku`(sudokuBookWorld: SudokuBookWorld) {
        // TODO: Implement me
    }

    @Given("solve sudoku call returns same solved sudoku")
    fun `sudoku book file with collection of unsolved sudoku`() {
        // TODO: Implement me
    }

    @When("call solve sudoku")
    fun `call solve sudoku`(sudokuBookWorld: SudokuBookWorld) {
        // TODO: Implement me
    }

    @Then("sudoku book file with collection of solved sudoku is created")
    fun `sudoku book file with collection of solved sudoku is created`(sudokuBookWorld: SudokuBookWorld) {
        // TODO: Implement me
    }

    @Then("each solved sudoku is stored in the database with expected solvedCount")
    fun `each solved sudoku is stored in the database with expected solvedCount`(sudokuBookWorld: SudokuBookWorld) {
        // TODO: Implement me
    }
}

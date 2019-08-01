package com.workshop.sudokubook.stepDefs

import com.workshop.sudokubook.collection.SudokuBookWorld
import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import cucumber.api.java.en.When

class SudokuBookComponentStepDefs {

    @Given("sudoku book file with collection of unsolved sudoku")
    fun `sudoku book file with collection of unsolved sudoku`(sudokuBookWorld: SudokuBookWorld) {
    }

    @When("solve sudoku")
    fun `solve sudoku`() {
    }

    @Then("sudoku book file with collection of solved sudoku is created")
    fun `sudoku book file with collection of solved sudoku is created`() {
    }

    @Then("each solved sudoku is stored in the database with {int} solvedCount")
    fun `each solved sudoku is stored in the database with {int} solvedCount`(int1: Int?) {
    }

}

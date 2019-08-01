package com.workshop.sudokubook.stepDefs

import com.workshop.sudokubook.Fixture
import com.workshop.sudokubook.client.SudokuHttpClient
import com.workshop.sudokubook.cucumber.collection.SudokuShared
import cucumber.api.java.en.Then
import cucumber.api.java.en.When
import org.junit.Assert.assertEquals

class SudokuBookContractStepDefs(
    private val sudokuHttpClient: SudokuHttpClient,
    private val sudokuShared: SudokuShared
) {

    @When("make a REST API call sudoku V1 solve to sudoku service with unsolved sudoku")
    fun `make a REST API call sudoku V1 solve to sudoku service with unsolved sudoku`() {
        val sudokuBookRequest = Fixture.SudokuBook.sudokuBookRequest()
        sudokuShared.sudokuBookResponse = sudokuHttpClient.solveSudoku(sudokuBookRequest)
    }

    @Then("response contains solved sudoku")
    fun `response contains solved sudoku`() {
        assertEquals(Fixture.SudokuBook.sudokuBookResponse(), sudokuShared.sudokuBookResponse)
    }
}

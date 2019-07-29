package com.workshop.sudokubook.client

import com.workshop.sudokubook.collections.SudokuRequest
import com.workshop.sudokubook.collections.SudokuResponse
import com.workshop.sudokubook.properties.SudokuConfigProperties
import org.springframework.stereotype.Component

@Component
class SudokuHttpClient(
    private val commonHttpClient: CommonHttpClient,
    private val sudokuConfigProperties: SudokuConfigProperties
) {

    fun solveSudoku(sudokuRequest: SudokuRequest): SudokuResponse {
        return commonHttpClient.post("${sudokuConfigProperties.host}/sudoku/v1/solve", sudokuRequest, SudokuResponse::class)
    }
}

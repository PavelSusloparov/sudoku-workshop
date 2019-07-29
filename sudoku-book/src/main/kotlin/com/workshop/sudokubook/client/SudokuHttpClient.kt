package com.workshop.sudokubook.client

import com.workshop.sudokubook.collections.SudokuBookResponse
import com.workshop.sudokubook.collections.SudokuRequest
import com.workshop.sudokubook.properties.SudokuConfigProperties
import org.springframework.stereotype.Component

@Component
class SudokuHttpClient(
    private val commonHttpClient: CommonHttpClient,
    private val sudokuConfigProperties: SudokuConfigProperties
) {

    fun solveSudoku(sudokuRequest: SudokuRequest): SudokuBookResponse {
        return commonHttpClient.post("${sudokuConfigProperties.host}/sudoku/v1/solve", sudokuRequest, SudokuBookResponse::class)
    }
}

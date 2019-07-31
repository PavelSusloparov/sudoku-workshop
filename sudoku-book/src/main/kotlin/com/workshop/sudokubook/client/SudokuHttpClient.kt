package com.workshop.sudokubook.client

import com.workshop.sudokubook.collections.SudokuBookRequest
import com.workshop.sudokubook.collections.SudokuBookResponse
import com.workshop.sudokubook.properties.SudokuConfigProperties
import org.springframework.stereotype.Component

@Component
class SudokuHttpClient(
    private val commonHttpClient: CommonHttpClient,
    private val sudokuConfigProperties: SudokuConfigProperties
) {

    fun solveSudoku(sudokuBookRequest: SudokuBookRequest): SudokuBookResponse {
        return commonHttpClient.post("${sudokuConfigProperties.host}/sudoku/v1/solve", sudokuBookRequest, SudokuBookResponse::class)
    }
}

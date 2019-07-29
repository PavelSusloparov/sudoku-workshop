package com.workshop.sudokubook.services

import com.workshop.sudokubook.client.SudokuHttpClient
import com.workshop.sudokubook.collections.SudokuBookRequest
import com.workshop.sudokubook.collections.SudokuRequest
import com.workshop.sudokubook.controllers.SudokuBookController
import org.springframework.stereotype.Component
import java.util.logging.Logger

@Component
class SudokuBookService(
    private val sudokuHttpClient: SudokuHttpClient
) {

    companion object {
        val logger: Logger = Logger.getLogger(this::class.java.simpleName)
        val sudokuString = """
            |5.6|8..|2..
            |...|45.|.8.
            |.43|..1|...
            |---+---+---
            |.78|5..|6.2
            |...|.7.|...
            |6.1|..8|37.
            |---+---+---
            |...|3..|72.
            |.2.|.64|...
            |..5|..2|4.1
        """.trimIndent()
    }

    fun create(): String {
        logger.info { "Create solved sudoku book" }
        // TODO: Read sudoku book with unsolved sudokus
        // TODO: Solve sudokus by calling /sudoku/v1/solve
        val sudokuRequest = SudokuRequest(sudokuString)
        val sudokuBookResponse = sudokuHttpClient.solveSudoku(sudokuRequest)
        logger.info { "sudokuBookResponse: $sudokuBookResponse" }
        // TODO: Produce result file with solved sudokus
        // TODO: Return absolute path to the file
        return sudokuBookResponse.result
    }
}

package com.workshop.sudoku.services

import org.springframework.stereotype.Component
import java.util.logging.Logger

@Component
class SudokuService {

    companion object {
        val logger: Logger = Logger.getLogger(this::class.java.simpleName)
    }

    fun solveV1(sudoku: String) = try {
            val board = sudoku.toBoard()
            val result = board.solve().first().toString()
            logger.info("Solved sudoku: \n$result")
            result
        } catch (e: Exception) {
            val errorMessage = "Failed to solveV1 sudoku"
            logger.severe("$errorMessage ${e.printStackTrace()}")
            errorMessage
        }

    fun solveV2(sudoku: String, size: Int): String {
        // TODO: Verify that board size can be only 2 or 3. Return error message - supported board size is 2 or 3, if not.
        // TODO: Verify that board matches size. Return error message - board doesn't match the size, if not.
        return sudoku
    }
}

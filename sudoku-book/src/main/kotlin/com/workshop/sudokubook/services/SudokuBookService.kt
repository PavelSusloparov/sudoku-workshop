package com.workshop.sudokubook.services

import com.workshop.sudokubook.client.SudokuHttpClient
import com.workshop.sudokubook.collections.SudokuRequest
import com.workshop.sudokubook.dao.SudokuTrackerDao
import com.workshop.sudokubook.entity.SudokuTrackerEntity
import org.springframework.stereotype.Component
import java.io.File
import java.util.logging.Logger

@Component
class SudokuBookService(
    private val sudokuHttpClient: SudokuHttpClient,
    private val sudokuTrackerDao: SudokuTrackerDao
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

    /**
     * Create a solved sudoku book
     */
    fun create() {
        val sudokuList = read()
        sudokuList.forEach {
            val solvedSudoku = solve(it)
            save(solvedSudoku)
        }
    }

    /**
     * Read a sudoku book
     */
    fun read(): List<String> {
        return try {
            val sudokuBook = javaClass.classLoader.getResource("files/sudoku-book.txt")?.readText()!!
            val sudokuList = sudokuBook.split("============".toRegex())
            sudokuList.forEach {
                logger.info { "sudokuList: $it" }
            }
            sudokuList
        } catch (ex: Exception) {
            logger.severe { "Failed to read the sudoku book. Exception: $ex" }
            emptyList()
        }
    }

    /**
     * Solve a sudoku
     */
    fun solve(sudoku: String): String? {
        return try {
            val sudokuRequest = SudokuRequest(sudokuString)
            val sudokuBookResponse = sudokuHttpClient.solveSudoku(sudokuRequest)
            logger.info { "sudokuBookResponse: $sudokuBookResponse" }
            sudokuBookResponse.result
        } catch (ex: Exception) {
            logger.severe { "Failed to solve the sudoku. Exception: $ex" }
            null
        }
    }

    /**
     * Save a sudoku
     */
    fun save(sudoku: String?) {
        if (sudoku.isNullOrBlank()) {
            logger.warning("Sudoku is null. Skip")
            return
        }
        saveToFile(sudoku)
        saveToDatabase(sudoku)
    }

    /**
     * Save sudoku to a file
     */
    fun saveToFile(sudoku: String) {
        val file = File("files/sudoku-book.txt")
        file.writeText(sudoku)
        file.writeText("============")
    }

    /**
     * Save sudoku to the changelog
     */
    fun saveToDatabase(sudoku: String) {
        var sudokuTrackerEntity = sudokuTrackerDao.findBySudoku(sudoku)
        if (sudokuTrackerEntity == null) {
            sudokuTrackerEntity = SudokuTrackerEntity(sudoku = sudoku, solveCounter = 0)
        } else {
            sudokuTrackerEntity.solveCounter = sudokuTrackerEntity.solveCounter + 1
        }
        sudokuTrackerDao.save(sudokuTrackerEntity)
    }
}

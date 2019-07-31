package com.workshop.sudokubook.services

import com.workshop.sudokubook.client.SudokuHttpClient
import com.workshop.sudokubook.collections.SudokuBookRequest
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
        val sudokuList = transform()
        sudokuList.forEach {
            val solvedSudoku = solve(it)
            save(solvedSudoku)
        }
    }

    /**
     * Read a sudoku book
     */
    fun read() = javaClass.classLoader.getResource("files/sudoku-book.txt")?.readText()

    /**
     * Transform a sudoku book to collection of sudokus
     */
    fun transform(): List<String> {
        return try {
            val sudokuBook = read()
            val sudokuList = sudokuBook?.split("============\n".toRegex()) ?: emptyList()
            sudokuList.forEach {
                logger.info { "sudokuList: $it" }
            }
            sudokuList
        } catch (ex: Exception) {
            logger.severe { "Failed to transform the sudoku book. Exception: ${ex.printStackTrace()}" }
            emptyList()
        }
    }

    /**
     * Solve a sudoku
     */
    fun solve(sudoku: String): String? {
        return try {
            val sudokuBookRequest = SudokuBookRequest(sudokuString)
            val sudokuBookResponse = sudokuHttpClient.solveSudoku(sudokuBookRequest)
            logger.info { "sudokuBookResponse: $sudokuBookResponse" }
            sudokuBookResponse.result
        } catch (ex: Exception) {
            logger.severe { "Failed to solve the sudoku. Exception: ${ex.printStackTrace()}" }
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
        val filePath = javaClass.classLoader.getResource("files/sudoku-book.txt")?.path ?: throw Exception("Failed to find initial sudoku book")
        val file = File(filePath.replace("sudoku-book.txt", "sudoku-book-solved.txt"))
        file.writeText(sudoku)
        file.writeText("============")
    }

    /**
     * Save sudoku to the changelog
     */
    fun saveToDatabase(sudoku: String): SudokuTrackerEntity {
        var sudokuTrackerEntity = sudokuTrackerDao.findBySudoku(sudoku)
        if (sudokuTrackerEntity == null) {
            sudokuTrackerEntity = SudokuTrackerEntity(sudoku = sudoku, solveCounter = 0)
        } else {
            sudokuTrackerEntity.solveCounter = sudokuTrackerEntity.solveCounter + 1
        }
        return sudokuTrackerDao.save(sudokuTrackerEntity)
    }
}

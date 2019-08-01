package com.workshop.sudokubook.services

import com.workshop.sudokubook.client.SudokuHttpClient
import com.workshop.sudokubook.collections.SudokuBookRequest
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
    fun create(filePath: String) {
        val sudokuList = transform(filePath)
        val sb = StringBuilder()
        sudokuList.forEach {
            val solvedSudoku = solve(it)
            sb.append(solvedSudoku)
            sb.append("\n============\n")
            saveToDatabase(solvedSudoku)
        }
        save(sb.toString(), getAbsoluteResultFilePath(filePath))
    }

    /**
     * Read a sudoku book
     */
    fun read(filePath: String) = javaClass.classLoader.getResource(filePath)?.readText()

    /**
     * Transform a sudoku book to collection of sudoku
     */
    fun transform(filePath: String): List<String> {
        val sudokuBook = read(filePath)
        val sudokuList = sudokuBook?.split("============\n".toRegex()) ?: emptyList()
        sudokuList.forEach {
            logger.info { "sudokuList: $it" }
        }
        return sudokuList
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
    fun save(sudoku: String?, filePath: String) {
        if (sudoku.isNullOrBlank()) {
            logger.warning("Sudoku is null. Skip")
            return
        }
        saveToFile(sudoku, filePath)
    }

    /**
     * Save sudoku to a file
     */
    fun saveToFile(sudoku: String, filePath: String) {
        val file = File(filePath)
        file.writeText(sudoku)
    }

    private fun getAbsoluteResultFilePath(filePath: String): String {
        val absoluteFilePath = javaClass.classLoader.getResource(filePath)?.path
            ?: throw Exception("Failed to find initial sudoku book")
        return absoluteFilePath.replace(".txt", "-solved.txt")
    }

    /**
     * Save sudoku to the changelog
     */
    fun saveToDatabase(sudoku: String?): SudokuTrackerEntity? {
        if (sudoku.isNullOrBlank()) {
            logger.warning("Sudoku is null. Skip")
            return null
        }
        var sudokuTrackerEntity = sudokuTrackerDao.findBySudoku(sudoku)
        if (sudokuTrackerEntity == null) {
            sudokuTrackerEntity = SudokuTrackerEntity(sudoku = sudoku, solveCounter = 1)
        } else {
            sudokuTrackerEntity.solveCounter = sudokuTrackerEntity.solveCounter + 1
        }
        return sudokuTrackerDao.save(sudokuTrackerEntity)
    }
}

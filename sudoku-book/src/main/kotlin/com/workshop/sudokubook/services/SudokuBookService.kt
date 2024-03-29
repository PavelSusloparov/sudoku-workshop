package com.workshop.sudokubook.services

import com.workshop.sudokubook.client.SudokuHttpClient
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
        // TODO: Implement me
        return ""
    }

    /**
     * Save list of sudoku to a file
     */
    fun save(sudoku: String?, filePath: String) {
        // TODO: Implement me
    }

    /**
     * Save sudoku to a file
     */
    fun saveToFile(sudoku: String, filePath: String) = File(filePath).writeText(sudoku)

    fun getAbsoluteResultFilePath(filePath: String): String {
        val absoluteFilePath = javaClass.classLoader.getResource(filePath)?.path
            ?: throw Exception("Failed to find initial sudoku book")
        return absoluteFilePath.replace(".txt", "-solved.txt")
    }

    /**
     * Save sudoku to the changelog
     */
    fun saveToDatabase(sudoku: String?): SudokuTrackerEntity? {
        // TODO: Implement me
        return null
    }
}

package com.workshop.sudokubook.services

import org.springframework.stereotype.Component
import java.util.logging.Logger

@Component
class SudokuBookService {

    companion object {
        val logger: Logger = Logger.getLogger(this::class.java.simpleName)
    }

    fun create(): String {
        return ""
    }
}

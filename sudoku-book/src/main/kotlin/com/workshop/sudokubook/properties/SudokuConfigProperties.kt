package com.workshop.sudokubook.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "sudoku")
class SudokuConfigProperties {

    lateinit var host: String
}

package com.workshop.sudoku.collections

import org.jetbrains.annotations.NotNull

class SudokuRequest {

    @NotNull(value = "sudoku is required")
    val sudoku: String? = null
}

class SudokuResponse(var result: String)

package com.workshop.sudokubook.collections

import org.jetbrains.annotations.NotNull

class SolveSudokuRequest{

    @NotNull(value = "filePath is required")
    var filePath: String? = null
}

class SudokuBookRequest(var sudoku: String)

class SudokuBookResponse(val result: String? = null)

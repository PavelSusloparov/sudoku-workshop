package com.workshop.sudokubook.cucumber.collection

import com.workshop.sudokubook.collections.SudokuBookResponse
import org.springframework.stereotype.Component

@Component
class SudokuShared(var sudokuBookResponse: SudokuBookResponse? = null)

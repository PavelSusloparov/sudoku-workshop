package com.workshop.sudokubook.controllers

import com.workshop.sudokubook.collections.SudokuBookRequest
import com.workshop.sudokubook.collections.SudokuBookResponse
import com.workshop.sudokubook.services.SudokuBookService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sudoku-book")
class SudokuBookController(
    private val sudokuBookService: SudokuBookService
) {

    /**
     * Create sudoku book V1
     */
    @PostMapping("/v1/create", consumes = [ MediaType.APPLICATION_JSON_UTF8_VALUE ], produces = [ MediaType.APPLICATION_JSON_UTF8_VALUE ])
    fun solveV1(@Validated @RequestBody req: SudokuBookRequest): ResponseEntity<SudokuBookResponse> {
        val result = sudokuBookService.create()
        return ResponseEntity(SudokuBookResponse(result = result), HttpStatus.OK)
    }
}

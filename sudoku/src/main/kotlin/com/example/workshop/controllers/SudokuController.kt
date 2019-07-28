package com.example.workshop.controllers

import com.example.workshop.collections.SudokuRequest
import com.example.workshop.collections.SudokuResponse
import com.example.workshop.services.SudokuService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sudoku")
class SudokuController(
    private val sudokuService: SudokuService
) {

    /**
     * Solve sudoku V1
     */
    @PostMapping("/v1/solve", consumes = [ MediaType.APPLICATION_JSON_UTF8_VALUE ], produces = [ MediaType.APPLICATION_JSON_UTF8_VALUE ])
    fun solveV1(@Validated @RequestBody req: SudokuRequest): ResponseEntity<SudokuResponse> {
        val result = sudokuService.solveV1(req.sudoku!!)
        return ResponseEntity(SudokuResponse(result = result), HttpStatus.OK)
    }

    /**
     * Solve sudoku V2
     */
    @PostMapping("/v2/solve", consumes = [ MediaType.APPLICATION_JSON_UTF8_VALUE ], produces = [ MediaType.APPLICATION_JSON_UTF8_VALUE ])
    fun solveV2(@Validated @RequestBody req: SudokuRequest): ResponseEntity<SudokuResponse> {
        val result = sudokuService.solveV2(req.sudoku!!, 3)
        return ResponseEntity(SudokuResponse(result = result), HttpStatus.OK)
    }
}

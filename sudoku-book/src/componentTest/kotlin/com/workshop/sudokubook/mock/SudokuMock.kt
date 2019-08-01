package com.workshop.sudokubook.mock

import com.github.tomakehurst.wiremock.client.WireMock
import org.springframework.stereotype.Component

@Component
class SudokuMock {

    fun solveSudoku() {
        val solveSudokuResponse = javaClass.classLoader.getResource("json/solve-sudoku.json")?.readText()
            ?: throw Exception("Response file does not exist")
        WireMock.stubFor(
            WireMock.post(WireMock.urlMatching("/sudoku/v1/solve"))
                .willReturn(
                    WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(solveSudokuResponse)
                )
        )
    }
}

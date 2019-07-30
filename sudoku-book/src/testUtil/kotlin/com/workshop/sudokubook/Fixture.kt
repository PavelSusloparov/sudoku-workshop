package com.workshop.sudokubook

import com.workshop.sudokubook.collections.SudokuResponse

class Fixture {

    class Sudoku {
        companion object {

            fun sudokuResponse() = SudokuResponse(
                result = """
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
            )
        }
    }
}

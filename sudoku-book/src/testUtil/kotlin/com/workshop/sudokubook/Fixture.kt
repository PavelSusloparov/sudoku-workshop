package com.workshop.sudokubook

import com.workshop.sudokubook.collections.SudokuBookRequest
import com.workshop.sudokubook.collections.SudokuBookResponse
import com.workshop.sudokubook.collections.SudokuRequest
import com.workshop.sudokubook.collections.SudokuResponse
import com.workshop.sudokubook.entity.SudokuTrackerEntity

class Fixture {

    class Sudoku {
        companion object {

            fun sudokuRequest() = SudokuRequest(
                sudoku = """
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
            fun sudokuResponse() = SudokuResponse(
                result = """
                    596|837|214
                    217|456|983
                    843|921|567
                    ---+---+---
                    978|513|642
                    432|679|158
                    651|248|379
                    ---+---+---
                    184|395|726
                    729|164|835
                    365|782|491
                """.trimIndent()
            )
        }
    }

    class SudokuBook {
        companion object {

            fun sudokuBookRequest() = SudokuBookRequest(
                sudoku = """
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

            fun sudokuBookResponse() = SudokuBookResponse(
                result = """
                    596|837|214
                    217|456|983
                    843|921|567
                    ---+---+---
                    978|513|642
                    432|679|158
                    651|248|379
                    ---+---+---
                    184|395|726
                    729|164|835
                    365|782|491
                """.trimIndent()
            )

            fun sudokuTrackerEntity() = SudokuTrackerEntity(
                sudoku = """
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
                """.trimIndent(),
                solveCounter = 0
            )
        }
    }
}

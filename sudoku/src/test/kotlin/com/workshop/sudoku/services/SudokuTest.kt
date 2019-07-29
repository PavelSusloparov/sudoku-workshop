package com.workshop.sudoku.services

import com.workshop.sudoku.services.Sudoku.Board
import com.workshop.sudoku.services.Sudoku.Cell
import com.workshop.sudoku.services.Sudoku.Point
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.ArrayList

class SudokuTest {

    companion object {
        fun String.toBoard(): Board {
            val cells = replace(Regex("[|\\-+\n]"), "").mapTo(ArrayList()) { c ->
                if (c == '.') Cell() else Cell(c.toString().toInt())
            }
            return Board(cells)
        }
    }

    @Test
    fun `solve sudoku example try 1`() {
        val board = """
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
        """.trimMargin().toBoard()

        assertThat(board.solve().first(), equalTo("""
            |596|837|214
            |217|456|983
            |843|921|567
            |---+---+---
            |978|513|642
            |432|679|158
            |651|248|379
            |---+---+---
            |184|395|726
            |729|164|835
            |365|782|491
        """.trimMargin().toBoard()))

        assertTrue(false)
    }

    @Test
    fun `solve sudoku example from readme`() {
        val board = """
            |..4|8..|.17
            |67.|9..|...
            |5.8|.3.|..4
            |---+---+---
            |3..|74.|1..
            |.69|...|78.
            |..1|.69|..5
            |---+---+---
            |1..|.8.|3.6
            |...|..6|.91
            |24.|..1|5..
        """.trimMargin().toBoard()

        assertThat(board.solve().first(), equalTo("""
            |934|825|617
            |672|914|853
            |518|637|924
            |---+---+---
            |325|748|169
            |469|153|782
            |781|269|435
            |---+---+---
            |197|582|346
            |853|476|291
            |246|391|578
        """.trimMargin().toBoard()))
    }

    @Test fun `solve medium sudoku from dailysudoku-dot-com`() {
        // http://dailysudoku.com/sudoku/archive/2016/11/2016-11-6_solution.shtml
        val board = """
            |6..|...|2.3
            |...|4.3|8..
            |.3.|7..|..9
            |---+---+---
            |...|.2.|1..
            |49.|...|.65
            |..6|.9.|...
            |---+---+---
            |1..|..5|.8.
            |..9|6..|...
            |8.4|...|..2
        """.trimMargin().toBoard()

        assertThat(board.solve().first(), equalTo("""
            |641|859|273
            |927|413|856
            |538|762|419
            |---+---+---
            |785|326|194
            |492|187|365
            |316|594|728
            |---+---+---
            |163|245|987
            |279|638|541
            |854|971|632
        """.trimMargin().toBoard()))
    }

    @Test fun `solve hard sudoku from dailysudoku-dot-com`() {
        // http://dailysudoku.com/sudoku/archive/2016/11/2016-11-5_solution.shtml
        val board = """
            |.7.|..1|...
            |19.|6.5|...
            |84.|.7.|.9.
            |---+---+---
            |9..|..8|5..
            |5.7|...|8.1
            |..1|5..|..2
            |---+---+---
            |.5.|.2.|.19
            |...|9.4|.86
            |...|1..|.5.
        """.trimMargin().toBoard()

        assertThat(board.solve().first(), equalTo("""
            |275|891|634
            |193|645|728
            |846|372|195
            |---+---+---
            |964|218|573
            |527|439|861
            |381|567|942
            |---+---+---
            |658|723|419
            |712|954|386
            |439|186|257
        """.trimMargin().toBoard()))
    }

    @Test fun `solve very hard sudoku from dailysudoku-dot-com`() {
        // http://dailysudoku.com/sudoku/archive/2016/11/2016-11-7_solution.shtml
        val board = """
            |.1.|...|..4
            |7.9|..1|..5
            |..5|9.7|..6
            |---+---+---
            |3.4|...|.2.
            |...|3.2|...
            |.7.|...|9.3
            |---+---+---
            |9..|8.6|4..
            |2..|5..|3.1
            |1..|...|.6.
        """.trimMargin().toBoard()

        assertThat(board.solve().first(), equalTo("""
            |816|235|794
            |729|461|835
            |435|987|216
            |---+---+---
            |384|659|127
            |591|372|648
            |672|148|953
            |---+---+---
            |953|816|472
            |267|594|381
            |148|723|569
        """.trimMargin().toBoard()))
    }

    @Test fun `square coordinates of a point`() {
        assertThat(Point(1, 2).square(), equalTo(listOf(
            Point(0, 0), Point(1, 0), Point(2, 0),
            Point(0, 1), Point(1, 1), Point(2, 1),
            Point(0, 2), Point(1, 2), Point(2, 2)
        )))
        assertThat(Point(0, 8).square(), equalTo(listOf(
            Point(0, 6), Point(1, 6), Point(2, 6),
            Point(0, 7), Point(1, 7), Point(2, 7),
            Point(0, 8), Point(1, 8), Point(2, 8)
        )))
        assertThat(Point(7, 7).square(), equalTo(listOf(
            Point(6, 6), Point(7, 6), Point(8, 6),
            Point(6, 7), Point(7, 7), Point(8, 7),
            Point(6, 8), Point(7, 8), Point(8, 8)
        )))
    }

    @Test fun `convert string to board`() {
        val board = """
            |..4|8..|.17
            |67.|9..|...
            |5.8|.3.|..4
            |---+---+---
            |3..|74.|1..
            |.69|...|78.
            |..1|.69|..5
            |---+---+---
            |1..|.8.|3.6
            |...|..6|.91
            |24.|..1|5..
        """.trimMargin().toBoard()

        board.apply {
            assertThat(this[0, 0], equalTo(Cell()))
            assertThat(this[2, 0], equalTo(Cell(4)))
            assertThat(this[0, 2], equalTo(Cell(5)))
            assertThat(this[8, 7], equalTo(Cell(1)))
        }
    }

    @Test fun `convert board to string`() {
        val board = Board().apply {
            set(0, 0, Cell(9))
            set(4, 0, Cell(8))
            set(8, 0, Cell(7))
            set(0, 4, Cell(6))
            set(4, 4, Cell(5))
            set(8, 4, Cell(4))
            set(0, 8, Cell(3))
            set(4, 8, Cell(2))
            set(8, 8, Cell(1))
        }
        assertThat(board.toString(), equalTo("""
            |9..|.8.|..7
            |...|...|...
            |...|...|...
            |---+---+---
            |...|...|...
            |6..|.5.|..4
            |...|...|...
            |---+---+---
            |...|...|...
            |...|...|...
            |3..|.2.|..1
        """.trimMargin()))
    }
}

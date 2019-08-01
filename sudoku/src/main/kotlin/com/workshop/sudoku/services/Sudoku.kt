package com.workshop.sudoku.services

import com.workshop.sudoku.services.Sudoku.Board.Companion.size
import com.workshop.sudoku.services.Sudoku.Board.Companion.squareSize
import org.springframework.stereotype.Component

fun <T> ArrayList<T>.fill(n: Int, value: T): ArrayList<T> {
    repeat(1.rangeTo(n).count()) { add(value) }
    return this
}

fun <T> Iterable<T>.toSeq(): Sequence<T> {
    val iterator = this.iterator()
    return object : Sequence<T> {
        override fun iterator() = iterator
    }
}

fun String.toBoard(): Sudoku.Board {
    val cells = replace(Regex("[|\\-+\n]"), "").mapTo(ArrayList()) { c ->
        if (c == '.') Sudoku.Cell() else Sudoku.Cell(c.toString().toInt())
    }
    return Sudoku.Board(cells)
}

@Component
@Suppress("unused") // Because this class is a "namespace".
class Sudoku {

    data class Board(private val cells: ArrayList<Cell> = ArrayList<Cell>().fill(size * size, Cell())) {

        companion object {
            const val size = 9
            const val squareSize = size / 3
        }

        private val positionedCells: List<PositionedCell>
            get() = cells.mapIndexed { i, cell -> PositionedCell(Point(i % size, i / size), cell) }

        fun solve(): Sequence<Board> {
            optimizeGuesses()

            if (cells.any { it.isNotFilled() && it.guesses.isEmpty() }) return emptySequence()
            if (cells.all { it.isFilled() }) return sequenceOf(this)

            return positionedCells.find { it.cell.isNotFilled() }!!.let {
                it.cell.guesses.toSeq().flatMap { guess ->
                    copy().set(it.point.x, it.point.y, Cell(guess)).solve()
                }
            }
        }

        private fun optimizeGuesses() {
            fun cell(point: Point) = this[point.x, point.y]

            fun List<Point>.removeGuesses(value: Int) =
                filter { cell(it).isNotFilled() }
                    .forEach { set(it.x, it.y, cell(it).removeGuess(value)) }

            positionedCells
                .filter { it.cell.isFilled() }
                .forEach {
                    it.point.row().removeGuesses(it.cell.value!!)
                    it.point.column().removeGuesses(it.cell.value)
                    it.point.square().removeGuesses(it.cell.value)
                }
        }

        private fun copy(): Board = Board(ArrayList(cells))

        operator fun get(x: Int, y: Int): Cell = cells[x + y * size]

        operator fun set(x: Int, y: Int, cell: Cell): Board {
            cells[x + y * size] = cell
            return this
        }

        override fun toString(): String {
            fun <T> List<T>.slicedBy(sliceSize: Int): List<List<T>> =
                if (size <= sliceSize) listOf(this)
                else listOf(take(sliceSize)) + drop(sliceSize).slicedBy(sliceSize)

            fun <T> List<T>.mapJoin(separator: String, f: (T) -> String) =
                joinToString(separator) { f(it) }

            return positionedCells.slicedBy(size * squareSize).mapJoin("\n---+---+---\n") { section ->
                section.slicedBy(size).mapJoin("\n") { row ->
                    row.slicedBy(squareSize).mapJoin("|") { slice ->
                        slice.mapJoin("") {
                            if (it.cell.isFilled()) it.cell.value.toString() else "."
                        }
                    }
                }
            }
        }
    }

    private data class PositionedCell(val point: Point, val cell: Cell)

    data class Point(val x: Int, val y: Int) {
        fun row() = 0.until(size).map { Point(it, y) }

        fun column() = 0.until(size).map { Point(x, it) }

        fun square(): List<Point> {
            fun rangeOfSquare(coordinate: Int): IntRange {
                return (coordinate / squareSize).let {
                    (it * squareSize).until((it + 1) * squareSize)
                }
            }
            return rangeOfSquare(y).flatMap { cellY ->
                rangeOfSquare(x).map { cellX ->
                    Point(cellX, cellY)
                }
            }
        }
    }

    data class Cell(val value: Int?, val guesses: List<Int>) {
        constructor() : this(null, 1.rangeTo(size).toList())
        constructor(value: Int) : this(value, emptyList())

        fun isFilled() = value != null
        fun isNotFilled() = !isFilled()
        fun removeGuess(value: Int) = if (guesses.isEmpty()) this else copy(guesses = guesses - value)
    }
}

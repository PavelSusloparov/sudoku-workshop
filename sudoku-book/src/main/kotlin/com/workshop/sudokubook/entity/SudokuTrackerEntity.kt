package com.workshop.sudokubook.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Inheritance
import javax.persistence.InheritanceType
import javax.persistence.Table

@Entity
@Table(name = "sudoku_tracker")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
class SudokuTrackerEntity(

    @Column(name = "sudoku", nullable = false)
    var sudoku: String,

    @Column(name = "solve_counter", nullable = false)
    var solveCounter: Int = 0

) : BaseEntity()

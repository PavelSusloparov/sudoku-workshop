package com.workshop.sudokubook.dao

import com.workshop.sudokubook.entity.EntityDao
import com.workshop.sudokubook.entity.SudokuTrackerEntity
import org.springframework.stereotype.Component

@Component
class SudokuTrackerDao : EntityDao<SudokuTrackerEntity>(SudokuTrackerEntity::class) {

    fun findBySudoku(sudoku: String): SudokuTrackerEntity? {
        return em.createQuery("select ste from SudokuTrackerEntity ste where ste.sudoku=:sudoku",
            SudokuTrackerEntity::class.java)
            .setParameter("sudoku", sudoku)
            .resultList.firstOrNull()
    }
}

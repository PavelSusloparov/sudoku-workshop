Feature:
  As a sudoku enthusiast I would like to get answers for all sudoku in the book, so I can compare my answer with expected answers.

  Scenario Outline: Solve entire sudoku book
    Given sudoku book file with collection of unsolved sudoku
    | unsolvedSudokuPath   |
    | <unsolvedSudokuPath> |
    When solve sudoku
    Then sudoku book file with collection of solved sudoku is created
    And each solved sudoku is stored in the database with 0 solvedCount

    Examples:
      | unsolvedSudokuPath          |
      | "files/sudoku-book-one.txt" |
      | "files/sudoku-book.txt"     |

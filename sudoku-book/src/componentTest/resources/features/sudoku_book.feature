Feature:
  As a sudoku enthusiast I would like to get answers for all sudoku in the book, so I can compare my answer with expected answers.

  Scenario Outline: Solve entire sudoku book
    Given sudoku book file with collection of unsolved sudoku
      | unsolvedSudokuPath   |
      | <unsolvedSudokuPath> |
    Given solve sudoku call returns same solved sudoku
    When call solve sudoku
      | unsolvedSudokuPath   |
      | <unsolvedSudokuPath> |
    Then sudoku book file with collection of solved sudoku is created
      | solvedSudokuPath   |
      | <solvedSudokuPath> |
    And each solved sudoku is stored in the database with expected solvedCount
      | numberOfDatabaseRecords   | solveCounter   |
      | <numberOfDatabaseRecords> | <solveCounter> |

    Examples:
      | unsolvedSudokuPath             | solvedSudokuPath                      | numberOfDatabaseRecords | solveCounter |
      | files/sudoku-book-test-one.txt | files/sudoku-book-test-one-solved.txt | 1                       | 1            |
      | files/sudoku-book-test.txt     | files/sudoku-book-test-solved.txt     | 1                       | 5            |

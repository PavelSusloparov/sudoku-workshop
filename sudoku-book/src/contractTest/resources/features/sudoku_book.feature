Feature:
  As a sudoku enthusiast I would like to get answers for all sudoku in the book, so I can compare my answer with expected answers.

  Scenario: Verify solve sudoku
    When make a REST API call /sudoku/v1/solve to sudoku service with unsolved sudoku
    Then response contains solved sudoku

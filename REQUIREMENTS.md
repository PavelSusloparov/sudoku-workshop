# Requirements

## Feature 1

As a sudoku enthusiast I would like to get answers for all sudoku in the book, so I can compare my answer with expected answers.

Acceptance criteria:
* Sudoku book should contain at least 5 puzzles
* Solved sudoku book should have file name with -solved postfix
* There should be ability to keep track of solved sudoku count over time

Technical considerations:
* Rest API JSON interface

Sudoku input:
* Sudoku size is 3x3
* Sudoku can not have missed lines
* Sudoku can not have repeated numbers in one line or column
* Numbers can be from 1 to 9 only

## Feature 2

As a kid sudoku enthusiast I would like to have the sudoku book with smaller size sudoku, so I can solve easier sudoku.

Acceptance criteria:
- Sudoku size is 2

## Bug fix for Sudoku service

* Exception handling for sudoku service.
* Return appropriate message, if sudoku has missed lines
* Return appropriate message, if sudoku has repeated numbers in one line or column
* Return appropriate message, if sudoku has numbers not from the allowed range 1..9

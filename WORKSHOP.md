# Workshop

## Step 1

1. Implement Feature 1 in [requirements document](REQUIREMENTS.md).

### Approach

Apply TDD practise:
* Start with writing component test
* Add just enough methods in application in order to test to run and fail
* Write unit test
* Add application logic
* Repeat, until you have one step working
* Work on another step, eventually component test should pass

For Component test, stub Sudoku service with WireMock.

For Contract tests call Sudoku service API.

For Unit tests use Mockito to mock other classes dependencies.

### Outcome

* Have 1 endpoint, which generates a Sudoku book with answers.
* Have component, contract, unit tests written.

## Step 2

Implement Feature 2 in [requirements document](REQUIREMENTS.md).

### Approach

Similar approach for step 1 with additional complexity of sudoku service.

## Step 3

Work on bug fix from [Bug fix for Sudoku service](REQUIREMENTS.md) section.


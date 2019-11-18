# Testing workshop

The repo is an example of integration between 2 applications with Rest API JSON. Integration includes best practices, such as:

* Contract testing with Cucumber and JUnit
* Component testing with WireMock, Cucumber and JUnit
* Unit testing with Mockito and JUnit
* Unit tests code coverage with Jacoco
* API documentation with OpenAPI specification

## Workshop agenda

We provide digital entertainment services for the global market. Our first product is Sudoku Book.
More about sudoku [here](https://en.wikipedia.org/wiki/Sudoku)
Our first feature is to create automatic Sudoku Book solver to help people be better at the game.

## Technical Pre-requirements

1. Install [docker](https://docs.docker.com/docker-for-mac/install/)

2. Install [docker-compose](https://github.com/Yelp/docker-compose/blob/master/docs/install.md)

3. Follow Artifactory secrets setup for [RedTech Environment Setup](https://connect.we.co/display/FL/How+to+Set+Up+Your+Local+RedTech+Development+Environment "RedTech Environment Setup")

4. Clone this repository

5. In IntelijIdea. File -> New -> Module from Existing Source -> Gradle -> Choose sudoku

6. In IntelijIdea. File -> New -> Module from Existing Source -> Gradle -> Choose sudoku-book

## Services start

```bash
./run.sh
```

## Verification
Check that all containers are running

```bash
docker ps
```

### Sudoku service

Enter URL in your browser
```bash
http://0.0.0.0:8102/actuator/health
```
You should see - {"status": "UP"}

### Sudoku Book service

Enter URL in your browser
```bash
http://0.0.0.0:8103/actuator/health
```
You should see - {"status": "UP"}

## Begin Workshop

[Continue here](WORKSHOP.md)


# Testing workshop

The repo is example of integration between 2 applications with Rest API JSON. Integration includes best practises, such as:

* Contract testing with Cucumber and JUnit
* Component testing with WireMock, Cucumber and JUnit
* Unit testing with Mockito and JUnit
* Unit tests code coverage with Jacoco
* API documentation with OpenAPI specification

## Pre-requirements

1. Install [docker](https://docs.docker.com/docker-for-mac/install/)

2. Install [docker-compose](https://github.com/Yelp/docker-compose/blob/master/docs/install.md)

3. Follow Artifactory secrets setup for [RedTech Environment Setup](https://connect.we.co/display/FL/How+to+Set+Up+Your+Local+RedTech+Development+Environment "RedTech Environment Setup")

4. Clone this repository

## Services start

```bash
./run.sh
```

## Verification

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


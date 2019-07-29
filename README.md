# cucumber-workshop

Cucumber + WireMock example

## Pre-requirements

1. Install [docker](https://docs.docker.com/docker-for-mac/install/)

2. Install [docker-compose](https://github.com/Yelp/docker-compose/blob/master/docs/install.md)

3. Clone this repository

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


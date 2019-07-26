# cucumber-workshop

Cucumber + WireMock example

## Pre-requirements

1. Install [docker](https://docs.docker.com/docker-for-mac/install/)

2. Install [docker-compose](https://github.com/Yelp/docker-compose/blob/master/docs/install.md)

3. Clone this repository

## Services start

```bash
docker-compose up
```

## Verification

Enter URL in your browser
```bash
http://0.0.0.0:8102/actuator/health
```
You should see - {"status": "UP"}


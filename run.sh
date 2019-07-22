#!/usr/bin/env bash

./gradlew jibDockerBuild
docker-compose up

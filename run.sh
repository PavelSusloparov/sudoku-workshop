#!/usr/bin/env bash

ROOT_DIR=`dirname $0`

# Generate image for calculator application
cd ${ROOT_DIR}/sudoku
./gradlew jibDockerBuild

cd ${ROOT_DIR}
# Start both applications
docker-compose up

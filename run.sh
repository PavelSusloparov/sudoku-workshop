#!/usr/bin/env bash

ROOT_DIR=`dirname $0`

# Generate image for Sudoku application
cd ${ROOT_DIR}/sudoku
./gradlew jibDockerBuild

# Generate image for Sudoku Book application
cd ${ROOT_DIR}/sudoku-book
./gradlew jibDockerBuild

cd ${ROOT_DIR}
# Start both applications
docker-compose up

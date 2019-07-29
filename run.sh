#!/usr/bin/env bash

ROOT_DIR=`dirname $0`
cd ${ROOT_DIR}
ROOT_DIR=`pwd`

# Generate image for Sudoku application
cd ${ROOT_DIR}/sudoku
./gradlew jibDockerBuild

cd ${ROOT_DIR}/sudoku-book
# Generate image for Sudoku Book application
./gradlew jibDockerBuild

cd ${ROOT_DIR}
# Start both applications
docker-compose up

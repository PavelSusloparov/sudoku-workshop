#!/usr/bin/env bash

ROOT_DIR=`dirname $0`

# Generate image for calculator application
cd ${ROOT_DIR}/calculator
calculator/gradlew jibDockerBuild

# Start both applications
docker-compose up

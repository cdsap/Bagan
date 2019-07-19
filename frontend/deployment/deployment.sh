#!/bin/sh

cd ..

./gradlew clean build

cp ./build/libs/bagan-monitor.jar ../docker/frontend/

cd ../docker/frontend/

docker build . --tag bagan-monitor

docker tag bagan-monitor cdsap/bagan-monitor

docker push cdsap/bagan-monitor

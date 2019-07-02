#/bin/bash

cd ..

./gradlew clean build

cp ./build/libs/bagan-monitor.jar deployment

cd deployment

docker build . --tag bagan-monitor

docker tag bagan-monitor cdsap/bagan-monitor

docker push cdsap/bagan-monitor
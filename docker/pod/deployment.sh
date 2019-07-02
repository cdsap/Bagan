#/bin/bash

docker build . --tag=bagan-pod-injector

docker tag bagan-pod-injector cdsap/bagan-pod-injector

docker push cdsap/bagan-pod-injector



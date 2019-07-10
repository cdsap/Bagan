#/bin/bash

cp -R ../../k8s/ .

docker build . --tag=bagan-init

docker tag bagan-init cdsap/bagan-init

docker push cdsap/bagan-init

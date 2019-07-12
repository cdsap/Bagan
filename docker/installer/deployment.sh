#/bin/bash
docker build . --tag=bagan-init
docker tag bagan-init cdsap/bagan-init
docker push cdsap/bagan-init

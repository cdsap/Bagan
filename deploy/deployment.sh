#/bin/bash
VERSION="0.1.0"
IMAGE=""
#master deployment

# 1- Generate files
cd ../kscript
./gradlew build
rm -rf ../docker/installer/bin/*
rm -rf ../docker/pod/bin/*
rm -rf ../docker/pod/bin/*
mv  kscript/creator ../docker/installer/bin
mv  kscript/properties/ ../docker/pod/bin
mv  kscript/injector/ ../docker/pod/bin

# 2- Generate docker images
# 2.1- Bagan Installer
cd ../docker/installer
docker build . --tag=bagan-init
docker tag bagan-init cdsap/bagan-init
docker push cdsap/bagan-init
# 2.2- Bagan Pod
cd ../pod
docker build . --tag=bagan-pod-injector
docker tag bagan-pod-injector cdsap/bagan-pod-injector
docker push cdsap/bagan-pod-injector

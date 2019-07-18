#/bin/bash
VERSION="0.1.1"
REGISTRY=""
IMAGE_BAGAN_INIT="cdsap/bagan-init"
IMAGE_BAGAN_POD="cdsap/bagan-pod-injector"
set -e
# 1- Removing previous binaries on docker bin folders
rm -rf ../docker/installer/bin/*
rm -rf ../docker/pod/bin/*

# 2- Execute build of kscript to generated formatted files
cd ../kscript
./gradlew clean build


# 3- Move files to binary files
mv  build/kscript/creator/ ../docker/installer/bin/
mv  build/kscript/properties/ ../docker/pod/bin
mv  build/kscript/injector/ ../docker/pod/bin


# 4- Generate docker images
# 4.1- Bagan Installer
cd ../docker/installer
docker build . --tag=bagan-init
docker tag bagan-init $IMAGE_BAGAN_INIT:$VERSION
docker push $IMAGE_BAGAN_INIT:$VERSION

# 4.2- Bagan Pod
cd ../pod
docker build . --tag=bagan-pod-injector
docker tag bagan-pod-injector $IMAGE_BAGAN_POD:$VERSION
docker push $IMAGE_BAGAN_POD:$VERSION

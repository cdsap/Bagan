#!/bin/sh

function gcloudBagan(){
  PATH_GCLOUD_BAGAN="docker/installer/bin/creator"
  gcloud_bagan="kscript $PATH_GCLOUD_BAGAN/BaganGenerator.kt"
  echo "$gcloud_bagan;"
}

function gcloudDockerBagan(){
  gcloud_docker_bootstrap_bagan="kscript Bootstraping.kt"
  gcloud_docker_bagan="kscript BaganGenerator.kt"
  echo "$gcloud_docker_bootstrap_bagan;"
  echo "$gcloud_docker_bagan;"
}

function minikubeBagan(){
  PATH_MINIKUBE_BAGAN="docker/installer/bin"
  minikube_bagan="kscript $PATH_MINIKUBE_BAGAN/BaganGenerator.kt"
  echo $minikube_bagan;
}

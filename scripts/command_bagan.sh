#!/bin/sh

function gcloudBagan(){
  PATH_GCLOUD_BAGAN="tmp/creator"
  gcloud_bagan="kscript $PATH_GCLOUD_BAGAN/BaganGenerator.kt tmp"
  echo "$gcloud_bagan;"
}

function removeExperiments(){
  removingPreviousExperiments="helm del --purge \$( helm ls --all experimen* --short)"
  echo "$removingPreviousExperiments;"
}

function gcloudDockerBagan(){
  gcloud_docker_bootstrap_bagan="kscript Bootstraping.kt"
  gcloud_docker_bagan="kscript BaganGenerator.kt .."
  bashrc="source /root/.bashrc"
  changeFolder="cd tmp/creator"
  echo "$bashrc;"
  echo "$changeFolder;"
  echo "$gcloud_docker_bootstrap_bagan;"
  echo "$gcloud_docker_bagan"
}

function minikubeBagan(){
  PATH_MINIKUBE_BAGAN="tmp/creator"
  minikube_bagan="kscript $PATH_MINIKUBE_BAGAN/BaganGenerator.kt"
  echo $minikube_bagan;
}

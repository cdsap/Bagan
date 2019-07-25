#!/bin/sh

function gcloudBagan(){
  PATH_GCLOUD_BAGAN="tmp/creator"
  gcloud_bagan="kscript $PATH_GCLOUD_BAGAN/BaganGenerator.kt tmp"
  echo "$gcloud_bagan;"
}


function removeExperiments(){
  removingPreviousExperiments="helm del --purge \$( kubectl get pods -l type=experiment -o custom-columns=:metadata.name)"
  echo "$removingPreviousExperiments;"
}

function gcloudDockerBagan(){
  removingPreviousExperiments="helm del --purge \$( kubectl get pods -l type=experiment -o custom-columns=:metadata.name)"
  gcloud_docker_bootstrap_bagan="kscript Bootstraping.kt"
  gcloud_docker_bagan="kscript BaganGenerator.kt .."
  bashrc="source /root/.bashrc"
  change="cd tmp/creator"
  echo "pwd;"
  echo "$removingPreviousExperiments;"
  echo "$bashrc;"
  echo "$change;"
  echo "$gcloud_docker_bootstrap_bagan;"
  echo "pwd;"
  echo "$gcloud_docker_bagan"
}

function minikubeBagan(){
  PATH_MINIKUBE_BAGAN="tmp/creator"
  minikube_bagan="kscript $PATH_MINIKUBE_BAGAN/BaganGenerator.kt"
  echo $minikube_bagan;
}

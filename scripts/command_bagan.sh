#!/bin/bash

# Commands to execute emperiments inside Bagan with the Kscript file BaganGenerator.kt
# Used by modes gcloud, gcloud_docker and standalone.

function bagan(){
  PATH_GCLOUD_BAGAN="tmp/generator"
  execution_bagan="kscript $PATH_GCLOUD_BAGAN/BaganGenerator.kt tmp"
  echo "$execution_bagan;"
}

function dockerBagan(){
  docker_bootstrap_bagan="kscript Bootstraping.kt"
  docker_bagan="kscript BaganGenerator.kt .."
  bashrc="source /root/.bashrc"
  changeFolder="cd tmp/generator"
  echo "$bashrc;"
  echo "$changeFolder;"
  echo "$docker_bootstrap_bagan;"
  echo "$docker_bagan"
}

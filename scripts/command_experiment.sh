#!/bin/sh

function experiment(){
  eval "$(bagan)"
}

function bagan(){
  PATH_GCLOUD_BAGAN="tmp/creator"
  gcloud_bagan="kscript $PATH_GCLOUD_BAGAN/BaganGenerator.kt tmp"
  echo "$gcloud_bagan;"
}

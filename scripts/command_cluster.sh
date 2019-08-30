#!/bin/bash

# Commands to execute gcloud initialization, cluster creation and credentials inside Bagan.
# Used by modes gcloud and mode_gcloud_docker.

function gcloudInit(){
  gcloud_init="gcloud init"
  gcloud_configure_docker="gcloud auth configure-docker"
  echo "$gcloud_init;"
  echo "$gcloud_configure_docker;"
}

function gcloudCreateCluster() {
  gcloud_create_cluster="gcloud container clusters create $cluster --zone $zone --machine-type=$machine"
  echo "$gcloud_create_cluster;"
}

function gcloudClusterCredentials(){
  gcloud_credentials="gcloud container clusters get-credentials $cluster --zone $zone"
  echo "$gcloud_credentials;"
}

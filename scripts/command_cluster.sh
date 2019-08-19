#!/bin/sh

function cluster(){
   eval "$(gcloudInit)"
   eval "$(gcloudCreateCluster)"
   eval "$(gcloudClusterCredentials)"
}

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

## deprecated?

function miniKubeInit(){
  minikube_init=""
}

function minikubeCredentials(){
  minikube_credentials=""
}

#!/bin/sh

function gcloudInit(){
  gcloud_init="gcloud init"
  gcloud_configure_docker="gcloud auth configure-docker"
  gcloud_create_cluster="gcloud container clusters create $cluster --zone $zone --machine-type=$machine"
  echo "$gcloud_init;"
  echo "$gcloud_configure_docker;"
  echo "$gcloud_create_cluster;"
}

function miniKubeInit(){
  minikube_init=""
}

function gcloudCredentials(){
  gcloud_credentials="gcloud container clusters get-credentials $cluster --zone $zone"
  echo "$gcloud_credentials;"
}

function minikubeCredentials(){
  minikube_credentials=""
}

#!/bin/sh

gcloud_init="gcloud init"
gcloud_configure_docker="gcloud auth configure-docker"
gcloud_create_cluster="gcloud container clusters create $clux --zone $zonex --machine-type=$machine"

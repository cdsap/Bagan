#!/bin/bash

# Commands to execute utility functions like removing experiments and retrieving IP of Grafana Dashboard.
# Used by mode gcloud, gcloud_docker and standalone.

function removeExperiments(){
  removingPreviousExperiments="helm delete \$( helm ls --all experimen* --short)"
  echo "$removingPreviousExperiments;"
}

function infoDashboard(){
  infoDashboard="kubectl get services bagan-grafana  -o custom-columns=:status.loadBalancer.ingress[0].ip"
  echo "$infoDashboard;"
}

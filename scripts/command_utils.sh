#!/bin/sh

function removeExperiments(){
  removingPreviousExperiments="helm del --purge \$( helm ls --all experimen* --short)"
  echo "$removingPreviousExperiments;"
}

function infoDashboard(){
  infoDashboard="kubectl get services bagan-grafana  -o custom-columns=:status.loadBalancer.ingress[0].ip"
  echo "$infoDashboard;"
}

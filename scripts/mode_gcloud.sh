#!/bin/sh

function gcloudExecutor(){
    log "Command $mode"
    if [ $mode == "cluster" ]; then
      eval "$(gcloudInit)"
      eval "$(gcloudCreateCluster)"
      eval "$(gcloudClusterCredentials)"
      eval "$(gcloudHelm)"
      eval "$(gcloudInfraPods)"
      eval "$(createSecret)"
      eval "$(gcloudBagan)"
   elif [ $mode == "infrastructure" ]; then
      eval "$(gcloudClusterCredentials)"
      eval "$(gcloudHelm)"
      eval "$(gcloudInfraPods)"
      eval "$(createSecret)"
      checkPreviousExperiments
      eval "$(gcloudBagan)"
   elif [ $mode == "experiment" ]; then
      eval "$(gcloudClusterCredentials)"
      #  eval "$(createSecret)"
      eval "$(gcloudBagan)"
   elif [[ $mode == "create_cluster" ]]; then
      eval "$(gcloudInit)"
      eval "$(gcloudCreateCluster)"
   elif [[ $mode == "credentials" ]]; then
      eval "$(gcloudClusterCredentials)"
   elif [[ $mode == "secret" ]]; then
      eval "$(gcloudClusterCredentials)"
      eval "$(createSecret)"
   elif [[ $mode == "helm" ]]; then
      eval "$(gcloudClusterCredentials)"
      eval "$(gcloudHelm)"
   elif [[ $mode == "helm_init" ]]; then
      eval "$(gcloudClusterCredentials)"
      eval "$(helmInit)"
   elif [[ $mode == "helm_clusterrolebinding" ]]; then
      eval "$(gcloudClusterCredentials)"
      eval "$(helmClusterRoleBinding)"
   elif [[ $mode == "infra_pods" ]]; then
      eval "$(gcloudClusterCredentials)"
      eval "$(gcloudInfraPods)"
   elif [[ $mode == "grafana" ]]; then
      eval "$(gcloudClusterCredentials)"
      eval "$(grafana)"
   elif [[ $mode == "influxdb" ]]; then
      eval "$(gcloudClusterCredentials)"
      eval "$(influxdb)"
   elif [[ $mode == "services" ]]; then
      eval "$(gcloudClusterCredentials)"
      eval "$(services)"
   elif [[ $mode == "remove_experiments" ]]; then
      eval "$(gcloudClusterCredentials)"
      eval "$(removeExperiments)"
   elif [[ $mode == "grafana_dashboard" ]]; then
      eval "$(gcloudClusterCredentials)"
      eval "$(infoDashboard)"
   else
      color '31;1' "Error no mode parsed properly for gcloud"
      exit 1
   fi
 }

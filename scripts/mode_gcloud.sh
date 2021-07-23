#!/bin/bash

function gcloudExecutor(){
    log "Command $command"
    if [ $command == "cluster" ]; then
      eval "$(gcloudInit)"
      eval "$(gcloudCreateCluster)"
      eval "$(gcloudClusterCredentials)"
      eval "$(infraPods)"
      eval "$(createSecret)"
      eval "$(bagan)"
      eval "$(endMessage)"
   elif [ $command == "infrastructure" ]; then
      eval "$(gcloudClusterCredentials)"
      eval "$(helmInstaller)"
      eval "$(infraPods)"
      eval "$(createSecret)"
      eval "$(bagan)"
      eval "$(endMessage)"
   elif [ $command == "experiment" ]; then
      eval "$(gcloudClusterCredentials)"
      eval "$(bagan)"
      eval "$(endMessage)"
   elif [[ $command == "create_cluster" ]]; then
      eval "$(gcloudInit)"
      eval "$(gcloudCreateCluster)"
      eval "$(endMessageSingleCommand)"
   elif [[ $command == "credentials" ]]; then
      eval "$(gcloudClusterCredentials)"
      eval "$(endMessageSingleCommand)"
   elif [[ $command == "secret" ]]; then
      eval "$(gcloudClusterCredentials)"
      eval "$(createSecret)"
      eval "$(endMessageSingleCommand)"
   elif [[ $command == "helm" ]]; then
      eval "$(gcloudClusterCredentials)"
      eval "$(helmInstaller)"
      eval "$(endMessageSingleCommand)"
   elif [[ $command == "helm_init" ]]; then
      eval "$(gcloudClusterCredentials)"
      eval "$(endMessageSingleCommand)"
   elif [[ $command == "helm_clusterrolebinding" ]]; then
      eval "$(gcloudClusterCredentials)"
      eval "$(helmClusterRoleBinding)"
      eval "$(endMessageSingleCommand)"
   elif [[ $command == "infra_pods" ]]; then
      eval "$(gcloudClusterCredentials)"
      eval "$(infraPods)"
      eval "$(endMessageSingleCommand)"
   elif [[ $command == "grafana" ]]; then
      eval "$(gcloudClusterCredentials)"
      eval "$(grafana)"
      eval "$(endMessageSingleCommand)"
   elif [[ $command == "influxdb" ]]; then
      eval "$(gcloudClusterCredentials)"
      eval "$(influxdb)"
      eval "$(endMessageSingleCommand)"
   elif [[ $command == "services" ]]; then
      eval "$(gcloudClusterCredentials)"
      eval "$(services)"
      eval "$(endMessageSingleCommand)"
   elif [[ $command == "remove_experiments" ]]; then
      eval "$(gcloudClusterCredentials)"
      eval "$(removeExperiments)"
      eval "$(endMessageSingleCommand)"
   elif [[ $command == "grafana_dashboard" ]]; then
      eval "$(gcloudClusterCredentials)"
      eval "$(infoDashboard)"
      eval "$(endMessageSingleCommand)"
   else
      color '31;1' "Error no command parsed properly for gcloud"
      exit 1
   fi
 }

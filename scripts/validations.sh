#!/bin/bash

if [ ! "$mode" = "gcloud" ] && [ ! "$mode" = "standalone" ] && [ ! "$mode" = "gcloud_docker" ]; then
  log ""
  color '31;1' "Error: mode not supported. Current clusters environments supported are standalone, gcloud and gcloud_docker:"
  log ""
  color '32;1' "Example: ./bagan gcloud cluster"
  color '32;1' "Example: ./bagan gcloud_docker infrastructure"
  color '32;1' "Example: ./bagan standalone experiment"
  log ""
  exit 1
fi

declare -a commands=( "cluster" "infrastructure" "experiment" "create_cluster"
"credentials" "secret" "helm" "helm_init" "helm_clusterrolebinding"
"infra_pods" "grafana" "influxdb" "services" "remove_experiments" "grafana_dashboard" )

for i in "${commands[@]}"
do
    if [ "$i" == "$command" ] ; then
        commandFound="Found"
    fi
done

if [ -z "$commandFound" ]; then
   log ""
   color '31;1' "Error: command not found." >&2
   log ""
   color '34;1' "Avaliable commands:"
   log "cluster: create cluster, infrastructure(helm + roles + grafana + influx) and execute the experiments."
   log "infrastructure: create infrastrucure for the cluster(helm + roles + grafana + influx) and execute the experiments."
   log "experiment: execute the experiments"
   log "create_cluster: create cluster"
   log "credentials: get credentials from gcloud"
   log "secret: create secret in cluster with the ssh key information provided"
   log "helm: initialize Helm service and create cluster role bindings"
   log "helm_init: initialize Helm service."
   log "helm_clusterrolebinding: create cluster role bindings."
   log "infra_pods: create release for grafana, influxdb and services"
   log "grafana: create release for grafana"
   log "influxdb: create release for influxdb"
   log "services: create services related to grafana"
   log "remove_experiments: remove existing experiment releases"
   log "grafana_dashboard: ip to access grafana dashboard(:3000)"
   log ""
   log "Examples"
   color '32;1' "./bagan gcloud cluster"
   color '32;1' "./bagan gcloud_docker experiment"
   color '32;1' "./bagan standalone remove_experiments"

   exit 1
fi

. scripts/validate_json.sh

#!/bin/sh

function standaloneExecutor(){
  if [ $mode == "cluster" ]; then
    printf '%s\n' "Not implemented"
  elif [ $mode == "infrastructure" ]; then
    printf '%s\n' "Mode infrastrucure"
    eval "$(gcloudHelm)"
    eval "$(gcloudInfraPods)"
    eval "$(createSecret)"
    checkPreviousExperiments
    eval "$(gcloudBagan)"
 elif [ $mode == "experiment" ]; then
    printf '%s\n' "Mode expreriment"
    eval "$(gcloudBagan)"
 elif [[ $mode == "create_cluster" ]]; then
    printf '%s\n' "Not implemented"
 elif [[ $mode == "credentials" ]]; then
    printf '%s\n' "Not implemented"
 elif [[ $mode == "secret" ]]; then
    eval "$(createSecret)"
 elif [[ $mode == "helm" ]]; then
    eval "$(gcloudHelm)"
 elif [[ $mode == "helm_init" ]]; then
    eval "$(helmInit)"
 elif [[ $mode == "helm_clusterrolebinding" ]]; then
    eval "$(helmClusterRoleBinding)"
 elif [[ $mode == "infra_pods" ]]; then
    eval "$(gcloudInfraPods)"
 elif [[ $mode == "grafana" ]]; then
    eval "$(grafana)"
 elif [[ $mode == "influxdb" ]]; then
    eval "$(influxdb)"
 elif [[ $mode == "services" ]]; then
    eval "$(services)"
 elif [[ $mode == "remove_experiments" ]]; then
    eval "$(removeExperiments)"
 elif [[ $mode == "grafana_dashboard" ]]; then
    eval "$(infoDashboard)"
 else
    color '31;1' "Error no mode parsed properly for standalone"
    exit 1
 fi
}

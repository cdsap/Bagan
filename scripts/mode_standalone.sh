#!/bin/bash

function standaloneExecutor(){
  if [ $command == "cluster" ]; then
    printf '%s\n' "Not implemented"
  elif [ $command == "infrastructure" ]; then
    printf '%s\n' "Mode infrastrucure"
    printf '%s\n' "111Mode infrastrucure"
    eval "$(helmInstaller)"
    printf '%s\n' "1Mode infrastrucure"
    eval "$(infraPods)"
    printf '%s\n' "1Mode infrastrucure"
    eval "$(createSecret)"
    eval "$(bagan)"
    eval "$(endMessage)"
 elif [ $command == "experiment" ]; then
    printf '%s\n' "Mode expreriment"
    eval "$(bagan)"
    eval "$(endMessage)"
 elif [[ $command == "create_cluster" ]]; then
    printf '%s\n' "Not implemented"
 elif [[ $command == "credentials" ]]; then
    printf '%s\n' "Not implemented"
 elif [[ $command == "secret" ]]; then
    eval "$(createSecret)"
    eval "$(endMessageSingleCommand)"
 elif [[ $command == "helm" ]]; then
    eval "$(helmInstaller)"
    eval "$(endMessageSingleCommand)"
 elif [[ $command == "helm_init" ]]; then
    eval "$(helmInit)"
    eval "$(endMessageSingleCommand)"
 elif [[ $command == "helm_clusterrolebinding" ]]; then
    eval "$(helmClusterRoleBinding)"
    eval "$(endMessageSingleCommand)"
 elif [[ $command == "infra_pods" ]]; then
    eval "$(infraPods)"
    eval "$(endMessageSingleCommand)"
 elif [[ $command == "grafana" ]]; then
    eval "$(grafana)"
    eval "$(endMessageSingleCommand)"
 elif [[ $command == "influxdb" ]]; then
    eval "$(influxdb)"
    eval "$(endMessageSingleCommand)"
 elif [[ $command == "services" ]]; then
    eval "$(services)"
    eval "$(endMessageSingleCommand)"
 elif [[ $command == "remove_experiments" ]]; then
    eval "$(removeExperiments)"
    eval "$(endMessageSingleCommand)"
 elif [[ $command == "grafana_dashboard" ]]; then
    eval "$(infoDashboard)"
    echo "1;"
    eval "$(endMessageSingleCommand)"
 else
    color '31;1' "Error no command parsed properly for standalone"
    exit 1
 fi
}

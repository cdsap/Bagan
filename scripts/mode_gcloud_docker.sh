#!/bin/sh

function gcloudDockerExecutor(){
  echo $mode
  if [ $mode == "cluster" ]; then
    printf '%s\n' "Mode cluster"
    execution="$(gcloudInit) $(gcloudClusterCredentials) $(gcloudHelm) $(gcloudInfraPods) $(gcloudDockerBagan)"
  elif [ $mode == "infrastructure" ]; then
    printf '%s\n' "Mode infrastrucure"
    execution="$(gcloudClusterCredentials) $(gcloudHelm) $(gcloudInfraPods) $(gcloudDockerBagan) "
  elif [ $mode == "experiment" ]; then
    printf '%s\n' "Mode experiment"
    execution="$(gcloudClusterCredentials) $(gcloudDockerBagan)"
  elif [[ $mode == "create_cluster" ]]; then
    execution="$(gcloudInit) $(gcloudCreateCluster)"
  elif [[ $mode == "credentials" ]]; then
    execution="$(gcloudClusterCredentials)"
  elif [[ $mode == "secret" ]]; then
    execution="$(secret)"
  elif [[ $mode == "helm" ]]; then
    execution="$(gcloudClusterCredentials) $(gcloudHelm)"
  elif [[ $mode == "helm_init" ]]; then
    execution="$(gcloudClusterCredentials) $(helmInit)"
  elif [[ $mode == "helm_clusterrolebinding" ]]; then
    execution="$(gcloudClusterCredentials) $(helmClusterRoleBinding)"
  elif [[ $mode == "infra_pods" ]]; then
    execution="$(gcloudClusterCredentials) $(gcloudInfraPods)"
  elif [[ $mode == "grafana" ]]; then
    execution="$(gcloudClusterCredentials) $(grafana)"
  elif [[ $mode == "influxdb" ]]; then
    execution="$(gcloudClusterCredentials) $(influxdb)"
  elif [[ $mode == "services" ]]; then
    execution="$(gcloudClusterCredentials) $(services)"
  elif [[ $mode == "remove_experiments" ]]; then
    execution="$(gcloudClusterCredentials) $(removeExperiments)"
  elif [[ $mode == "grafana_dashboard" ]]; then
    execution="$(gcloudClusterCredentials) $(infoDashboard)"
  else
    color '31;1' "Error no mode parsed properly for gcloud_docker: $mode"
    exit 1
  fi

  log "Executing Docker"
  docker run -ti -v $HOME/.config/gcloud:/root/.config/gcloud \
               -v $PWD/tmp:/usr/local/tmp \
               -v $PWD/bagan_conf.json:/usr/local/tmp/creator/bagan_conf.json \
                  cdsap/bagan-init:0.1.4 /bin/bash -c "
                  set -e;
                  export PATH=$PATH:/usr/local/gcloud/google-cloud-sdk/bin/;
                  cd /usr/local;
                  $execution"

}

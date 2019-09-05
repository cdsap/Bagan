#!/bin/sh

function gcloudDockerExecutor(){
  log "Command $command"
  if [ $command == "cluster" ]; then
    printf '%s\n' "Mode cluster"
    execution="$(gcloudInit) $(gcloudCreateCluster) $(gcloudClusterCredentials) $(helmInstaller) $(infraPods) $(createSecretDockerContainer) $(dockerBagan)"
  elif [ $command == "infrastructure" ]; then
    printf '%s\n' "Mode infrastrucure"
    execution="$(gcloudClusterCredentials) $(helmInstaller) $(infraPods) $(createSecretDockerContainer) $(dockerBagan) "
  elif [ $command == "experiment" ]; then
    printf '%s\n' "Mode experiment"
    execution="$(gcloudClusterCredentials) $(dockerBagan)"
  elif [[ $command == "create_cluster" ]]; then
    execution="$(gcloudInit) $(gcloudCreateCluster)"
  elif [[ $command == "credentials" ]]; then
    execution="$(gcloudClusterCredentials)"
  elif [[ $command == "secret" ]]; then
    execution="$(gcloudClusterCredentials) $(createSecretDockerContainer)"
  elif [[ $command == "helm" ]]; then
    execution="$(gcloudClusterCredentials) $(helmInstaller)"
  elif [[ $command == "helm_init" ]]; then
    execution="$(gcloudClusterCredentials) $(helmInit)"
  elif [[ $command == "helm_clusterrolebinding" ]]; then
    execution="$(gcloudClusterCredentials) $(helmClusterRoleBinding)"
  elif [[ $command == "infra_pods" ]]; then
    execution="$(gcloudClusterCredentials) $(infraPods)"
  elif [[ $command == "grafana" ]]; then
    execution="$(gcloudClusterCredentials) $(grafana)"
  elif [[ $command == "influxdb" ]]; then
    execution="$(gcloudClusterCredentials) $(influxdb)"
  elif [[ $command == "services" ]]; then
    execution="$(gcloudClusterCredentials) $(services)"
  elif [[ $command == "remove_experiments" ]]; then
    execution="$(gcloudClusterCredentials) $(removeExperiments)"
  elif [[ $command == "grafana_dashboard" ]]; then
    execution="$(gcloudClusterCredentials) $(infoDashboard)"
  else
    color '31;1' "Error no command parsed properly for gcloud_docker: $command"
    exit 1
  fi
  end="$(endMessageDocker)"
  log "Executing Docker"
  if [ "$private" == "true" ]; then

    docker run -ti -v $HOME/.config/gcloud:/root/.config/gcloud \
                 -v $PWD/tmp:/usr/local/tmp \
                 -v $PWD/bagan_conf.json:/usr/local/tmp/generator/bagan_conf.json \
                 -v $ssh:/root/.ssh/id_rsa \
                 -v $known_hosts:/root/.ssh/known_hosts \
                 cdsap/bagan-init:$dockerBaganInitVersion /bin/bash -c "
                 set -e;
                 export PATH=$PATH:/usr/local/gcloud/google-cloud-sdk/bin/;
                 cd /usr/local;
                 $execution
                 $end"
  else
    docker run -ti -v $HOME/.config/gcloud:/root/.config/gcloud \
       -v $PWD/tmp:/usr/local/tmp \
       -v $PWD/bagan_conf.json:/usr/local/tmp/generator/bagan_conf.json \
       cdsap/bagan-init:$dockerBaganInitVersion /bin/bash -c "
       set -e;
       export PATH=$PATH:/usr/local/gcloud/google-cloud-sdk/bin/;
       cd /usr/local;
       $execution
       echo 1;
       $end"
  fi

}

#!/bin/sh
extras=$1
echo ""
echo "Welcome to Bagan"
echo ""
echo "Be sure you have included the bagan_conf.json with the desired configuration."
echo ""
echo -n "Do you want to continue [y/n]: "
read ans

if [ $ans != 'y' ]; then
  exit 1
fi;

# include parse_yaml function
. validate_json.sh
. commands.sh
. gcloudinit.sh

#echo $execution

printf '%s\n' "Configuration file parsed."
printf '%s\n' ""
printf '%s\n' "Values:"
printf '%s\n' ""
printf '%s\n' "type: $type"
printf '%s\n' "cluster: $cluster"
printf '%s\n' "zone: $zone"
printf '%s\n' "machine: $machine"
printf '%s\n' "repository: $repository"
printf '%s\n' "gradleCommand: $gradleCommand"
printf '%s\n' "properties: $properties"
printf '%s\n' ""
printf '%s\n' "You have choosen run Bagan in $type environment"
if [ "$extras" = "executeExperiment" ]; then
   printf '%s\n' "You are going to execute directly experiments in cluster $cluster with zone $zone"
fi
printf '%s\n' ""
echo -n "Do you want to continue [y/n]: "
read ans2

if [ $ans2 != 'y' ]; then
  exit 1
fi;


printf '%s\n' "************************"
printf '%s\n' "************************"
printf '%s\n' "****Staring Bagan*****"
printf '%s\n' "$type"

if [ "$type" = "gcloud" ]; then

  if [ -z "$extras"]; then
    $gcloud_init
    $gcloud_configure_docker
    $gcloud_create_cluster
    $helm_init
    $helm_grafana
    $helm_influx
    $handleServices
  fi

  $executePods


elif  "$type" = "gcloud_docker" ]; then

    echo "This is gcloud_docker"

elif  "$type" = "minikube" ]; then

  echo "This is minikube"
else

  echo "error"
fi

#if [ "$type" = "gcloud" ]; then
#    echo "error"
#   docker run -ti -v $HOME/.config/gcloud:/root/.config/gcloud  \
#                  -v /Users/inaki/navetes/init/sa.json:/root/files/sa.json     cdsap/initbagan  /bin/bash -c "
#                  $loga;
#                  echo 'Grafana';
#                  $clean;
#                  $executePods
#                  echo 'Influxdb';
#                  kubectl get pods;

#                  kubectl get pod;
#                  echo 'fin';"

#fi

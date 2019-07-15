#!/bin/sh
extras=$1
echo ""
echo "Welcome to Bagan"
echo ""
echo ""
echo "Requeriments:
Installation with Gcloud:
   -- jq: https://stedolan.github.io/jq/
   -- gcloud: https://cloud.google.com/sdk/
   -- docker: https://www.docker.com/
   -- kscript: https://github.com/holgerbrandl/kscript

Installation with Docker:
   -- jq: https://stedolan.github.io/jq/
   -- docker: https://www.docker.com/

Installation with Minikube:
   -- jq: https://stedolan.github.io/jq/
   -- minikube: https://cloud.google.com/sdk/
   -- docker: https://www.docker.com/
   -- kscript: https://github.com/holgerbrandl/kscript
"
echo "Be sure you have included the bagan_conf.json with the desired configuration."
echo -n "Do you want to continue [y/n]: "
read ans



if [ "$ans" != "y" ] | [ -z "$ans" ]; then
  exit 1
fi;

# include parse_yaml function
. validate_json.sh
. commands.sh
. gcloudinit.sh
. helm.sh


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

if [ "$ans2" != "y" ] | [ -z "$ans2" ]; then
  exit 1
fi;


printf '%s\n' "************************"
printf '%s\n' "************************"
printf '%s\n' "****Staring Bagan*****"
printf '%s\n' "$type"

if [ "$type" = "gcloud" ]; then
   echo "1"
  if [ -z "$extras" ]; then
    echo "2"
    $gcloud_init
    echo "3"
    $gcloud_configure_docker
    echo "4"
    $gcloud_create_cluster
    echo "5"
    $helmInit
    echo "6"
    $helmServiceAccount
    echo "7"
    $helmClusterRole
    echo "8"
    sleep 10
    $helmPatchDeploy
    echo $helmPatchDeploy
    eval $helmPatchDeploy
    echo "9"
    sleep 10
    $helmClusterRole2
    echo "10"
    sleep 10
    $helmRepoUpdate
    echo "11"
    $helmGrafana
    echo "12"
    $helmInflux
    echo "13"
    eval $handleServices
  fi
echo "3"
cp bagan_conf.json ../docker/installer/creator/
cd ../docker/installer/creator/
$executePodsLocally


elif [ "$type" = "gcloud_docker" ]; then

   echo 1;
#   $gcloud_init;
#   echo 3;#
#   $gcloud_configure_docker;
#  $gcloud_create_cluster;
#$helmInit;
#echo 5;
#$helmServiceAccount;
#echo 6;
#$helmClusterRole;
#echo 7;
#$helmPatchDeploy;
#echo 8;
#sleep 10;
#$helmClusterRole2;
#echo 10;
#sleep 10;
#$helmRepoUpdate;
#echo 11;
#$helmGrafana;
#echo 12;
#$helmInflux;
#echo 14;
#$handleServices;


  if [ -z "$extras" ]; then
     a="echo 1111;
      $loga
      ls;
      pwd;
      echo 4;
    $executePods"
  fi
   echo "oposososososososososo";
     docker run -ti -v /Users/inaki/bagan_deployment/init/bagan_conf.json:/usr/local/creator/bagan_conf.json    cdsap/bagan-init  /bin/bash -c "
                    $a;
                    "
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

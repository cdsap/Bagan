#!/bin/bash

printf '%s\n' "************************"
printf '%s\n' "************************"
printf '%s\n' "****Parsing Json********"

typeJson=$(cat bagan_conf.json | jq -c -r '.bagan.type' | tr -d '\r')
repositoryJson=$(cat bagan_conf.json | jq -c -r '.bagan.repository' | tr -d '\r')
gradleCommandJson=$(cat bagan_conf.json | jq -c -r '.bagan.gradleCommand' | tr -d '\r')
propertiesJson=$(cat bagan_conf.json | jq -c -r '.bagan.experiments.properties' | tr -d '\r')
propertiesCountJson=$(cat bagan_conf.json | jq -c -r '.bagan.experiments.properties | length' | tr -d '\r')
clusterJson=$(cat bagan_conf.json | jq -c -r '.bagan.clusterName' | tr -d '\r')
zoneJson=$(cat bagan_conf.json | jq -c -r '.bagan.zone' | tr -d '\r')
machineJson=$(cat bagan_conf.json | jq -c -r  '.bagan.machine' | tr -d '\r')

printf '%s\n' "Json File parsed OK"

printf '%s\n' "************************"
printf '%s\n' "************************"

printf '%s\n' ""
printf '%s\n' ""


printf '%s\n' "************************"
printf '%s\n' "************************"
printf '%s\n' "****Validating Json*****"

if [ -z "$typeJson" ] | [ -z "$repositoryJson" ] | [ -z "$gradleCommandJson" ]
then
     printf '%s\n' "Error: repository, gradleCommand are required "
     exit 1
fi

if [ ! "$typeJson" = "gcloud" ] && [ ! "$typeJson" = "minikube" ] && [ ! "$typeJson" = "gcloud_docker" ]; then
  printf '%s\n' "Error: type $typeJson not supported. Current clusters environments supported are minikube, gcloud and gcloud_docker"
  exit 1
fi

regex='(https?|ftp|file)://[-A-Za-z0-9\+&@#/%?=~_|!:,.;]*[-A-Za-z0-9\+&@#/%=~_|]'
if [[ ! $repositoryJson =~ $regex ]]
then
  printf '%s\n' "Error: url repository is not correct "
  exit 1
fi


if [ -z "$propertiesJson" ]
then
     printf '%s\n' "Error: experiments are required in the main configuration file"
     exit 1
fi


if [ "$extras" = "executeExperiment" ]; then
   if [ "$typeJson" = "gcloud" ] | [ "$typeJson" = "gcloud_docker" ]; then
     if [ -z "$clusterJson" ] | [ -z "$zoneJson" ]
     then
         printf '%s\n' "Error: you have selected to execute experiments in G without Cluster and Zone Information"
         exit 1
     fi
   fi
fi

printf '%s\n' "Json File validated OK"

printf '%s\n' "************************"
printf '%s\n' "************************"

printf '%s\n' ""
printf '%s\n' ""


cluster=""
if [ -z "$clusterJson" ] | [ "$clusterJson" = "null" ]
then
   cluster="bagan"
else
   cluster=$clusterJson
fi

if [ -z "$zoneJson" ] | [ "$zoneJson" = "null" ]
then
   zone="us-central1-a"
else
   zone=$zoneJson
fi

if [ -z "$machineJson" ] | [ "$machineJson" = "null" ]
then
   machine="n1-standard-1"
else
   machine=$machineJson
fi

type=$typeJson
repository=$repositoryJson
gradleCommand=$gradleCommandJson
properties=$propertiesJson
propertiesCount=$propertiesCountJson

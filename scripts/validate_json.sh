#!/bin/sh

printf '%s\n' "Parsing Json..."
FILE="bagan_conf.json"
repositoryJson=$(cat $FILE | jq -c -r '.bagan.repository' | tr -d '\r')
gradleCommandJson=$(cat $FILE | jq -c -r '.bagan.gradleCommand' | tr -d '\r')
propertiesJson=$(cat $FILE | jq -c -r '.bagan.experiments.properties' | tr -d '\r')
propertiesCountJson=$(cat $FILE | jq -c -r '.bagan.experiments.properties | length' | tr -d '\r')
clusterJson=$(cat $FILE | jq -c -r '.bagan.clusterName' | tr -d '\r')
zoneJson=$(cat $FILE | jq -c -r '.bagan.zone' | tr -d '\r')
machineJson=$(cat $FILE | jq -c -r  '.bagan.machine' | tr -d '\r')

printf '%s\n' "Json File parsed OK"
printf '%s\n' "Validating Json..."

if  [ -z "$repositoryJson" ] || [ -z "$gradleCommandJson" ]
then
     printf '%s\n' "Error: repository, gradleCommand are required "
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
   if [ "$typeJson" = "gcloud" ] || [ "$typeJson" = "gcloud_docker" ]; then
     if [ -z "$clusterJson" ] || [ -z "$zoneJson" ]
     then
         printf '%s\n' "Error: you have selected to execute experiments in G without Cluster and Zone Information"
         exit 1
     fi
   fi
fi

printf '%s\n' "Json File validated OK"

cluster=""
if [ -z "$clusterJson" ] || [ "$clusterJson" = "null" ]
then
   cluster="bagan"
else
   cluster=$clusterJson
fi
echo $cluster
if [ -z "$zoneJson" ] || [ "$zoneJson" = "null" ]
then
   zone="us-central1-a"
else
   zone=$zoneJson
fi

if [ -z "$machineJson" ] || [ "$machineJson" = "null" ]
then
   machine="n1-standard-1"
else
   machine=$machineJson
fi

repository=$repositoryJson
gradleCommand=$gradleCommandJson
properties=$propertiesJson
propertiesCount=$propertiesCountJson

#!/bin/bash

type=$(cat sa.json | jq -c -r '.bagan.type' | tr -d '\r')
repository=$(cat sa.json | jq -c -r '.bagan.repository' | tr -d '\r')
gradleCommand=$(cat sa.json | jq -c -r '.bagan.gradleCommand' | tr -d '\r')
properties=$(cat sa.json | jq -c -r '.bagan.experiments.properties' | tr -d '\r')
propertiesCount=$(cat sa.json | jq -c -r '.bagan.experiments.properties | length' | tr -d '\r')
clusterName=$(cat sa.json | jq -c -r '.bagan.clusterName' | tr -d '\r')
zone=$(cat sa.json | jq -c -r '.bagan.zone' | tr -d '\r')

agp=$(cat sa.json | jq -c -r '.bagan.experiments.agp'  | tr -d '\r')
agpCount=$(cat sa.json | jq -c -r '.bagan.experiments.agp | length' | tr -d '\r')
gradle=$(cat sa.json | jq -c -r  '.bagan.experiments.gradle' | tr -d '\r')

if [ -z "$type" ] | [ -z "$repository" ] | [ -z "$gradleCommand" ]
then
     printf '%s\n' "Error: repository, gradleCommand are required "
     exit 1
fi


if [ ! "$type" = "gcloud" ] && [ ! "$type" = "minikube" ]; then
  printf '%s\n' "Error: type $type not supported. Current clusters environments supported are minikube and gcloud "
  exit 1
fi

regex='(https?|ftp|file)://[-A-Za-z0-9\+&@#/%?=~_|!:,.;]*[-A-Za-z0-9\+&@#/%=~_|]'
if [[ ! $repository =~ $regex ]]
then
  printf '%s\n' "Error: url repository is not correct "
  exit 1
fi


if [ -z "$properties" ] | [ -z "$agp" ] | [ -z "$gradle" ]
then
     printf '%s\n' "Error: experiments are required in the main configuration file"
     exit 1
fi

if [ "$agp" = "null" ] & [ "$gradle" = "null" ] & [ "$properties" = "null" ]
then
     printf '%s\n' "Error: experiments are required in the main configuration file"
     exit 1
fi
# todo check number of experiments

echo $properties
echo $propertiesCount
echo $agp
echo $agpCount
echo $gradle


printf '%s\n' "Json File parsed OK"

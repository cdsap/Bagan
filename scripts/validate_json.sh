#!/bin/bash

printf '%s\n' "Parsing Json..."
FILE="bagan_conf.json"
repositoryJson=$(cat $FILE | jq -c -r '.bagan.repository' | tr -d '\r')
gradleCommandJson=$(cat $FILE | jq -c -r '.bagan.gradleCommand' | tr -d '\r')
propertiesJson=$(cat $FILE | jq -c -r '.bagan.experiments.properties' | tr -d '\r')
propertiesCountJson=$(cat $FILE | jq -c -r '.bagan.experiments.properties | length' | tr -d '\r')
branchJson=$(cat $FILE | jq -c -r '.bagan.experiments.branch' | tr -d '\r')
branchCountJson=$(cat $FILE | jq -c -r '.bagan.experiments.branch | length' | tr -d '\r')
gradleWrapperVersionJson=$(cat $FILE | jq -c -r '.bagan.experiments.gradleWrapperVersion' | tr -d '\r')
gradleWrapperVersionCountJson=$(cat $FILE | jq -c -r '.bagan.experiments.gradleWrapperVersion | length' | tr -d '\r')
clusterJson=$(cat $FILE | jq -c -r '.bagan.clusterName' | tr -d '\r')
zoneJson=$(cat $FILE | jq -c -r '.bagan.zone' | tr -d '\r')
machineJson=$(cat $FILE | jq -c -r  '.bagan.machine' | tr -d '\r')
privateJson=$(cat $FILE | jq -c -r  '.bagan.private' | tr -d '\r')
sshJson=$(cat $FILE | jq -c -r  '.bagan.ssh' | tr -d '\r')
known_hostsJson=$(cat $FILE | jq -c -r  '.bagan.known_hosts' | tr -d '\r')
scenarioFileJson=$(cat $FILE | jq -c -r '.bagan.scenarioFile' | tr -d '\r')
scenarioNameJson=$(cat $FILE | jq -c -r '.bagan.scenarioName' | tr -d '\r')

log "Json File parsed OK"
log "Validating Json..."

if  [ -z "$repositoryJson" ] || [ -z "$gradleCommandJson" ]
then
     color '31;1' "Error: repository, gradleCommand are required "
     exit 1
fi

regex='^(https|git)(:\/\/|@)([^\/:]+)[\/:]([^\/:]+)\/(.+).git$'
if [[ ! $repositoryJson =~ $regex ]]
then
  color '31;1' "Error: url repository is not correct "
  exit 1
fi

if [ "$propertiesJson" == "null" ] && [ "$branchJson" == "null" ] && [ "$gradleWrapperVersionJson" == "null" ]; then
     color '31;1' "Error: you have to include at least one type experiment in the configuration file."
     log "Example:"
     log "\"experiments\": {
        \"properties\": [
           {
              \"name\": \"org.gradle.jvmargs\",
              \"options\": [\"-Xmx3g\",\"-Xmx4g\"]
           }
        ],
        \"branch\": [ \"develop\",\"master\"],
        \"gradleWrapperVersion\": [ \"5.6\",\"5.5\",\"5.4\"]
     }"
     exit 1
fi


if [ "$extras" = "executeExperiment" ]; then
   if [ "$typeJson" = "gcloud" ] || [ "$typeJson" = "gcloud_docker" ]; then
     if [ -z "$clusterJson" ] || [ -z "$zoneJson" ]
     then
         color '31;1' "Error: you have selected to execute experiments in G without Cluster and Zone Information"
         exit 1
     fi
   fi
fi

if [ "$privateJson" == "true" ]; then
  if [ -z "$sshJson" ] || [ "$sshJson" = "null" ]; then
    color '31;1' "Error: you have selected private repository but didn't include the ssh key path"
    exit 1
  fi
  if [ -z "$known_hostsJson" ] || [ "$known_hostsJson" = "null" ]; then
    color '31;1' "Error: you have selected private repository but didn't include the known_hosts path"
    exit 1
  fi
fi

log "Json File validated OK"

cluster=""
if [ -z "$clusterJson" ] || [ "$clusterJson" = "null" ]
then
   cluster="bagan"
else
   cluster=$clusterJson
fi

if [ -z "$zoneJson" ] || [ "$zoneJson" = "null" ]
then
   zone="us-west1-a"
else
   zone=$zoneJson
fi

if [ -z "$machineJson" ] || [ "$machineJson" = "null" ]
then
   machine="n1-standard-1"
else
   machine=$machineJson
fi

# Since Bagan 0.2 Gradle Profiler is used to execute the experimentation variants.
# We allow to use scenario files.
# Business rules to validate are:
#  a) If "scenarioFile" is defined it must exist
#  b) If scenarioName is empty null we need to use the gradleCommand task used in the configuration
#  c) If scenarioName is defined we need to validate that the name is included in the scenario file
# Check if the file exist

if [ -n "$scenarioFileJson" ]; then
  if [ -f "$scenarioFileJson" ]; then
     echo "sllsl $scenarioNameJson"
     if [ -n "$scenarioNameJson" ]; then
         echo "inaki2"
         echo "$scenarioNameJson"
         if grep -q $scenarioNameJson "$scenarioFileJson"; then
            scenarioName=$scenarioNameJson
         else
            # case c)
            color '31;1' "Error: you have specified a scenario file $scenarioFileJson, the scenario file exists, but the scenario name $scenarioFileName does not exist"
            exit 1
         fi
     fi
     scenarioName=$gradleCommandJson
     scenarioFile=$scenarioFileJson
  else
      # case a)
      color '31;1' "Error: you have specified a scenario file $scenarioFileJson but does not exist"
      exit 1
  fi
fi

repository=$repositoryJson
gradleCommand=$gradleCommandJson
properties=$propertiesJson
propertiesCount=$propertiesCountJson
branch=$branchJson
branchCount=$branchCountJson
gradleWrapperVersion=$gradleWrapperVersionJson
gradleWrapperVersionCountJson=$gradleWrapperVersionCountJson
private=$privateJson
ssh=$sshJson
known_hosts=$known_hostsJson

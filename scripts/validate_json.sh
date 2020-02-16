#!/bin/bash

printf '%s\n' "Parsing Json..."
FILE="bagan_conf.json"
repositoryJson=$(cat $FILE | jq -c -r '.bagan.repository' | tr -d '\r')
gradleCommandJson=$(cat $FILE | jq -c -r '.bagan.gradleCommand' | tr -d '\r')

clusterJson=$(cat $FILE | jq -c -r '.bagan.clusterName' | tr -d '\r')
zoneJson=$(cat $FILE | jq -c -r '.bagan.zone' | tr -d '\r')
machineJson=$(cat $FILE | jq -c -r  '.bagan.machine' | tr -d '\r')
privateJson=$(cat $FILE | jq -c -r  '.bagan.private' | tr -d '\r')
sshJson=$(cat $FILE | jq -c -r  '.bagan.ssh' | tr -d '\r')
known_hostsJson=$(cat $FILE | jq -c -r  '.bagan.known_hosts' | tr -d '\r')


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




# 1- Get both properties for experiments
incrementalChangesJson=$(cat $FILE | jq -c -r '.bagan.experiments.incrementalChanges' | tr -d '\r')
combinedJson=$(cat $FILE | jq -c -r '.bagan.experiments.combined' | tr -d '\r')

# 1.1 Validate at least one type of experiment is defined

if [ $incrementalChangesJson == "null" ] && [ $combinedJson == "null" ]; then
   printf '%s\n' "Error, you must define one type of experiment group at least"
   exit
fi

# 1.2 Validate two experiments are defined at the same time

if [ $incrementalChangesJson != "null" ] && [ $combinedsJson != "null" ]; then
  printf '%s\n' "Error, you have defined two experiment groups. You should use either BasicExperiments o IncrementalChanges"
  exit
fi

# 1.3 Let's validate the Incremental Changes experiments
if [ $incrementalChangesJson != "null" ]; then
  taskIncrementalJson=$(cat $FILE | jq -c -r '.bagan.experiments.incrementalChanges.taskIncremental' | tr -d '\r')
  iterationsExperimentJson=$(cat $FILE | jq -c -r '.bagan.experiments.incrementalChanges.iterationsExperiment' | tr -d '\r')

  if [ $taskIncrementalJson == "null" ]; then
    printf '%s\n' "Error, taskIncremental not found for Experiment Type: Incremental Changes"
    exit
  fi
  if [ $iterationsExperimentJson == "null" ]; then
    printf '%s\n' "Error, iterationsExperiment not found for Experiment Type: Incremental Changes"
    exit
  fi

  incrementalChangesValuesCountJson=$(cat $FILE | jq -c -r '.bagan.experiments.incrementalChanges.values | length' | tr -d '\r')
  incrementalChangesValuesJson=$(cat $FILE | jq -c -r '.bagan.experiments.incrementalChanges.values' | tr -d '\r')
  if [ $incrementalChangesValuesCountJson == "null" ] || [ $incrementalChangesValuesCountJson == "0" ]; then
    printf '%s\n' "Error, you should experiment at least one value for Experiment Type: Incremental Changes"
    exit
  fi

  printf '%s\n' "Experiment Type: Incremental Changes"
  printf '%s\n' "Task Incremental: $taskIncrementalJson"
  printf '%s\n' "Iterations: $iterationsExperimentJson"
  printf '%s\n' "Experiments: $incrementalChangesValuesJson"
fi


if [ $combinedJson != "null" ]; then
   propertiesJson=$(cat $FILE | jq -c -r '.bagan.experiments.combined.properties' | tr -d '\r')
   propertiesCountJson=$(cat $FILE | jq -c -r '.bagan.experiments.combined.properties | length' | tr -d '\r')
   branchJson=$(cat $FILE | jq -c -r '.bagan.experiments.combined.branch' | tr -d '\r')
   branchCountJson=$(cat $FILE | jq -c -r '.bagan.experiments.combined.branch | length' | tr -d '\r')
   gradleWrapperVersionJson=$(cat $FILE | jq -c -r '.bagan.experiments.combined.gradleWrapperVersion' | tr -d '\r')
   gradleWrapperVersionCountJson=$(cat $FILE | jq -c -r '.bagan.experiments.combined.gradleWrapperVersion | length' | tr -d '\r')
   if [ "$propertiesJson" == "null" ] && [ "$branchJson" == "null" ] && [ "$gradleWrapperVersionJson" == "null" ] && [ "$incrementalChangesJson" == "null" ] ; then
       color '31;1' "Error: you have to include at least one type experiment in the configuration file."
       log "Example:"
        ## TODO MISS TO INCLUDE THE NEW STRUCTURE OF COMBINED
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
     # exit 1
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

exit

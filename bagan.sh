#/bin/bash
type=$1
mode=$2
infra_type=$3

if [ ! "$type" = "gcloud" ] && [ ! "$type" = "minikube" ] && [ ! "$type" = "gcloud_docker" ]; then
  printf '%s\n' "Error: type not supported. Current clusters environments supported are minikube, gcloud and gcloud_docker."
  printf '%s\n' "Example: ./bagan gcloud cluster"
  printf '%s\n' "Example: ./bagan gcloud_docker infrastructure"
  printf '%s\n' "Example: ./bagan minkube experiment"
  exit 1
fi

if [ ! "$mode" = "cluster" ] && [ ! "$mode" = "infrastructure" ] && [ ! "$mode" = "experiment" ]; then
  printf '%s\n' "Error: mode not supported. Current modes supported are cluster, experiment and infrastructure."
  printf '%s\n' "cluster: create cluster in gcloud, initalizing the infrastructure and finally executing Bagan to generate the experiments"
  printf '%s\n' "infrastrucure: initialize the infrastrucure with helm and required charts of Grafana and InfluxDb"
  printf '%s\n' "experiment: execute Bagan to generate experiments in the environment selected"
  exit 1
fi

echo ""
echo "Welcome to Bagan"
echo ""

echo "Requeriments for your $type environment selection:"

if [ "$type" == "gcloud" ]; then
echo "   -- jq: https://stedolan.github.io/jq/
   -- gcloud: https://cloud.google.com/sdk/
   -- docker: https://www.docker.com/
   -- kscript: https://github.com/holgerbrandl/kscript"

elif [ "$type" == "gcloud_docker" ]; then
  echo "     -- jq: https://stedolan.github.io/jq/
     -- docker: https://www.docker.com/"

elif [ "$type" == "minikube" ]; then
  echo "   -- jq: https://stedolan.github.io/jq/
     -- minikube: https://cloud.google.com/sdk/
     -- docker: https://www.docker.com/
     -- kscript: https://github.com/holgerbrandl/kscript"
else
  echo "error"
fi
  #statements
  #statements

echo ""
echo "Be sure you have included the bagan_conf.json with the desired configuration."
echo -n "Do you want to continue [y/n]: "
read ans

if [ "$ans" != "y" ] | [ -z "$ans" ]; then
  exit 1
fi;

. scripts/validate_json.sh
. scripts/command_helm.sh
. scripts/command_bagan.sh
. scripts/command_init_credentials.sh


printf '%s\n' "Values:"
printf '%s\n' "type: $type"
printf '%s\n' "cluster: $cluster"
printf '%s\n' "zone: $zone"
printf '%s\n' "machine: $machine"
printf '%s\n' "repository: $repository"
printf '%s\n' "gradleCommand: $gradleCommand"
printf '%s\n' "properties: $properties"
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


function log() {
  echo "[LOG]: $1"
}


if [ $type == "gcloud" ]; then
  if [ $mode == "cluster" ]; then
    eval "$(gcloudInit)"
    eval "$(gcloudCredentials)"
    eval "$(gcloudHelm)"
    eval "$(gcloudInfraPods)"
    eval "$(gcloudBagan)"
  elif [ $mode == "infrastrucure" ]; then
    eval "$(gcloudCredentials)"
    eval "$(gcloudHelm)"
    eval "$(gcloudInfraPods)"
    eval "$(gcloudBagan)"
  elif [ $mode == "experiment" ]; then
    eval "$(gcloudCredentials)"
    eval "$(gcloudBagan)"
  else
    printf '%s\n' "Error no mode parsed properly for gcloud"
    exit 1
  fi
elif [ $type == "gcloud_docker" ]; then
  if [ $mode == "cluster" ]; then
    execution="$(gcloudInit); $(gcloudCredentials); $(gcloudHelm); $(gcloudInfraPods); $(gcloudDockerBagan);"
  elif [ $mode == "infrastructure" ]; then
    execution="$(gcloudCredentials); $(gcloudHelm); $(gcloudInfraPods); $(gcloudDockerBagan)"
  elif [ $mode == "experiment" ]; then
    execution="$(gcloudCredentials) $(gcloudDockerBagan) "
  else
    printf '%s\n' "Error no mode parsed properly for gcloud_docker"
    exit 1
  fi
  echo $execution
elif [ $type == "minikube" ]; then
  if [ $mode == "cluster"] || [ $mode == "infrastrucure" ]; then
    eval "$(gcloudHelm)"
    eval "$(gcloudInfraPods)"
    eval "$(gcloudBagan)"
  elif [ $mode == "experiment" ]; then
    eval "$(gcloudBagan)"
  else
    printf '%s\n' "Error no mode parsed properly for minikube"
    exit 1
  fi
else
  printf '%s\n' "Error no type parsed properly "
  exit 1
fi

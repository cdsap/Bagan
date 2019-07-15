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
  # todo
  # think if is this is suitable for our --man or --help
  #printf '%s\n' "Experiment:"
  #printf '%s\n' "Example: ./bagan gcloud experiment --> This command will execute Bagan in the cluster defined in bagan_conf.json with gcloud tool"
  #printf '%s\n' "Example: ./bagan gcloud_docker experiment --> This command will execute Bagan in the cluster defined in bagan_conf.json with docker"
  #printf '%s\n' "Example: ./bagan minikube experiment --> This command will execute Bagan in the cluster defined in bagan_conf.json within minikube"
  #printf '%s\n' "Infrastucture:"
  #printf '%s\n' "Example: ./bagan gcloud infrastrucure --> This command will initialize helm and will install the charts for Grafana and InfluxDb with gcloud tool"
  #printf '%s\n' "Example: ./bagan gcloud_docker infrastrucure --> This command will initialize helm and will install the charts for Grafana and InfluxDb within docker"
  #printf '%s\n' "Example: ./bagan minikube infrastrucure --> This command will initialize helm and will install the charts for Grafana and InfluxDb within minikube"
  exit 1
fi

# pending define ifra logic
# should something like if is defined and the parameterr is not null(infra_type)
# we need to check if is helm or grafana
#if [ "$mode" = "infrastructure" ]; then

#fi


echo $type
echo $mode
echo $infra_type

function log() {
  echo "[LOG]: $1"
}

function gcloudInit(){
  gcloud_init="gcloud init"
  gcloud_configure_docker="gcloud auth configure-docker"
  gcloud_create_cluster="gcloud container clusters create $cluster --zone $zone --machine-type=$machine"
  echo "$gcloud_init;"
  echo "$gcloud_configure_docker;"
  echo "$gcloud_create_cluster;"
}

function miniKubeInit(){
  minikube_init=""
}

function gcloudCredentials(){
  gcloud_credentials="gcloud container clusters get-credentials $cluster --zone $zone"
  echo "$gcloud_credentials;"
}

function minikubeCredentials(){
  minikube_credentials=""
}

# todo remember fit with spaaces
function gcloudHelm(){
  sleep10="sleep 10"
  sleep20="sleep 20"
  helm_init="helm init"
  helm_service_account="kubectl --namespace kube-system create serviceaccount tiller"
  helm_cluster_role="kubectl create clusterrolebinding tiller-cluster-rule --clusterrole=cluster-admin --serviceaccount=kube-system:tiller"
  helm_patch_deploy="kubectl --namespace kube-system patch deploy tiller-deploy -p '{\"spec\":{\"template\":{\"spec\":{\"serviceAccount\":\"tiller\"}}}}'"
  helm_cluster_role2="kubectl create clusterrolebinding cluster-admin-binding --clusterrole=cluster-admin --user=$(gcloud config get-value account)"
  helm_repo_update="helm repo update"
  echo "$helm_init;"
  echo "$helm_service_account;"
  echo "$sleep10;"
  echo "$helm_cluster_role;"
  echo "$helm_patch_deploy;"
  echo "$sleep20;"
  echo "$helm_cluster_role2;"
  echo "$helm_repo_update;"
}

function minikubeHelm(){
  helm_minikube_init="$helm_init"
  helm_minikube_repo_update="helm_repo_update"
}

function gcloudInfraPods(){
  PATH_GCLOUD_CHARTS=""
  gcloud_grafana="helm install -n bagan-grafana -f $PATH_GCLOUD_CHARTS/grafana/values.yaml $PATH__GCLOUD_CHARTS/grafana/"
  gcloud_influx="helm install -n bagan-influxdb -f $PATH_GCLOUD_CHARTS/influxdb/values.yaml $PATH_GCLOUD_CHARTS/influxdb/"
  echo "$gcloud_grafana;"
  echo "$gcloud_influx;"
}

function minikubeDockerInfraPods(){
  PATH_MINIKUBE_CHARTS=""
  gcloud_grafana="helm install -n bagan-grafana -f $PATH_MINIKUBE_CHARTS/grafana/values.yaml $PATH_MINIKUBE_CHARTS/grafana/"
  gcloud_influx="helm install -n bagan-influxdb -f $PATH_MINIKUBE_CHARTS/influxdb/values.yaml $PATH_PATH_MINIKUBE_CHARTSCHARTS/influxdb/"
}

function gcloudBagan(){
  PATH_GCLOUD_BAGAN=""
  gcloud_bagan="kscript $PATH_GCLOUD_BAGAN/Bagan.kt"
  echo "$gcloud_bagan;"
}

function gcloudDockerBagan(){
  gcloud_docker_bootstrap_bagan="kscript Bootstraping.kt;"
  gcloud_docker_bagan="kscript BaganGenerator.kt;"
  echo "$gcloud_docker_bootstrap_bagan;"
  echo "$gcloud_docker_bagan;"
}

function minikubeBagan(){
  PATH_MINIKUBE_BAGAN="."
  minikube_bagan="kscript $PATH_MINIKUBE_BAGAN/Bagan.kt"
  echo $minikube_bagan
}


function returningLiteral(){
  a="ls;"
  b="pwd;"
  echo "$a $b"


}

function executingLiteral(){
  echo "ls;"
  echo "cd ..;"
  echo "ls;"
}

if [ $type == "gcloud" ]; then
    if [ $mode == "cluster"]; then
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

     execution="$(gcloudInit); $(gcloudCredentials); $(gcloudHelm); $(gcloudInfraPods); $(gcloudBagan);"

  elif [ $mode == "infrastrucure" ]; then

    execution="$(gcloudCredentials); $(gcloudHelm); $(gcloudInfraPods); $(gcloudBagan);"

  elif [ $mode == "experiment" ]; then

    execution="$(gcloudCredentials); $(gcloudBagan); "

  else
    printf '%s\n' "Error no mode parsed properly for gcloud_docker"
    exit 1
  fi
  echo $execution
fi


returning="$(returningLiteral)"
executing="$(executingLiteral)"
#echo $returning
#eval $executing

a="$(gcloudInit)"
b="$(gcloudLog)"
c="$(gcloudHelm)"

d="$a $b $c"

echo $d

echo 1
#as=$(gcloudDockerInit)
#bs=$(gcloudDockerInit)
#log  "$as"
#eval $a
#echo 2
#echo $as

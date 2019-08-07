#!/bin/sh

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
  helm_minikube_init="helm init"
  helm_minikube_repo_update="helm repo update"
  echo "$helm_minikube_init"
  echo "$sleep15;"
  echo "$helm_minikube_repo_update"
}

function gcloudInfraPods(){
  PATH_GCLOUD_CHARTS="tmp"
  gcloud_grafana="helm install -n bagan-grafana -f $PATH_GCLOUD_CHARTS/grafana/values.yaml $PATH_GCLOUD_CHARTS/grafana/"
  gcloud_influx="helm install -n bagan-influxdb -f $PATH_GCLOUD_CHARTS/influxdb/values.yaml $PATH_GCLOUD_CHARTS/influxdb/"
  kubectl_bagan_service_grafana_remove="kubectl delete service bagan-grafana"
  kubectl_bagan_service_grafana_insert="kubectl expose deployment bagan-grafana --type=LoadBalancer"

  echo "$gcloud_grafana;"
  echo "$kubectl_bagan_service_grafana_remove;"
  echo "$kubectl_bagan_service_grafana_insert;"
  echo "$gcloud_influx;"

}

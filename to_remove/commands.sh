#!/bin/bash
gcloud create
gcloud log
gcloud install helm
gcloud install gcloud_infra_pods
gcloud




gcloud_init(){

}

gcloud_init
gcloud_log
gcloud_helm
gcloud_infra_pods
gcloud_bagan

gcloud_docker_init
gcloud_docker_log
gcloud_docker_helm
gcloud_docker_infra_pods
gcloud_docker_bagan

minikube_init
minikube_helm
minikube_infra_pods
minikube_bagan







helmInit="helm init; \
  kubectl --namespace kube-system create serviceaccount tiller; \
  kubectl create clusterrolebinding tiller-cluster-rule --clusterrole=cluster-admin --serviceaccount=kube-system:tiller; \
  sleep 10; \
  kubectl --namespace kube-system patch deploy tiller-deploy -p '{\"spec\":{\"template\":{\"spec\":{\"serviceAccount\":\"tiller\"}}}}';\
  kubectl create clusterrolebinding cluster-admin-binding --clusterrole=cluster-admin --user=$(gcloud config get-value account); \
  sleep 10; \
  helm repo update;"


handleServices="kubectl delete service bagan-grafana-experiments;
                    kubectl expose deployment bagan-grafana-experiments --type=LoadBalancer;
                    sleep 20;
                    kubectl port-forward \$(kubectl get pods -l app=bagan-influxdb -o jsonpath='{ .items[0].metadata.name }') 8086:8086 &
                 "

executePods="echo 1;
cd /usr/local/creator/;
kscript Hello.kt;
kscript BaganGenerator.kt"


executePodsLocally="
kscript ExperimentCoordinator.kt"


clean="helm del --purge \$( kubectl get pods -l type=experiment -o custom-columns=:metadata.name --field-selector=status.phase=Running)"


loga="gcloud container clusters get-credentials $cluster --zone $zone;"

# \n
#gcloud auth configure-docker \n
#gcloud container clusters create $clux --zone $zonex --machine-type=$machine; $loga"
#printf '%s\n' "888"

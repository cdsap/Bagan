#!/bin/sh

# Commands to execute the Helm requirements in a cluster. It contains the
# cluster role bindings required by the cluster(gcloud).
# Used by modes gcloud, gcloud_docker and standalone.

sleep10="sleep 10"
sleep20="sleep 20"
helm_init="helm init"
helm_service_account="kubectl --namespace kube-system create serviceaccount tiller"
helm_cluster_role="kubectl create clusterrolebinding tiller-cluster-rule --clusterrole=cluster-admin --serviceaccount=kube-system:tiller"
helm_patch_deploy="kubectl --namespace kube-system patch deploy tiller-deploy -p '{\"spec\":{\"template\":{\"spec\":{\"serviceAccount\":\"tiller\"}}}}'"
helm_cluster_role2="kubectl create clusterrolebinding cluster-admin-binding --clusterrole=cluster-admin --user=$(gcloud config get-value account)"
helm_repo_update="helm repo update"

function helm1(){
  echo "$helm_init;"
  echo "$helm_service_account;"
  echo "$sleep10;"
  echo "$helm_cluster_role;"
  echo "$helm_patch_deploy;"
  echo "$sleep20;"
  echo "$helm_cluster_role2;"
  echo "$helm_repo_update;"
}

function helmInit(){
  echo "$helm_init;"
}

function helmClusterRoleBinding (){
  echo "$helm_service_account;"
  echo "$sleep10;"
  echo "$helm_cluster_role;"
  echo "$helm_patch_deploy;"
  echo "$sleep20;"
  echo "$helm_cluster_role2;"
  echo "$helm_repo_update;"
}

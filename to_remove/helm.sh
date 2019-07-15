#!/bin/sh

helmInit="helm init"
helmServiceAccount="kubectl --namespace kube-system create serviceaccount tiller"
helmClusterRole="kubectl create clusterrolebinding tiller-cluster-rule --clusterrole=cluster-admin --serviceaccount=kube-system:tiller"
helmPatchDeploy="kubectl --namespace kube-system patch deploy tiller-deploy -p '{\"spec\":{\"template\":{\"spec\":{\"serviceAccount\":\"tiller\"}}}}'"
helmPatchDeploy2="kubectl --namespace kube-system patch deploy tiller-deploy -p '{\"spec\":{\"template\":{\"spec\":{\"serviceAccount\":\"tiller\"}}}}'"
helmClusterRole2="kubectl create clusterrolebinding cluster-admin-binding --clusterrole=cluster-admin --user=$(gcloud config get-value account)"
helmRepoUpdate="helm repo update"
helmInitc="helm init; \
  kubectl --namespace kube-system create serviceaccount tiller; \
  kubectl create clusterrolebinding tiller-cluster-rule --clusterrole=cluster-admin --serviceaccount=kube-system:tiller; \
  sleep 10; \
  kubectl --namespace kube-system patch deploy tiller-deploy -p '{\"spec\":{\"template\":{\"spec\":{\"serviceAccount\":\"tiller\"}}}}';\
  kubectl create clusterrolebinding cluster-admin-binding --clusterrole=cluster-admin --user=$(gcloud config get-value account); \
  sleep 10; \
  helm repo update;"


  helmGrafana="helm install -n bagan-grafana -f /root/files/k8s/grafana/values.yaml /root/files/k8s/grafana/"
  helmInflux="helm install -n bagan-influxdb -f /root/files/k8s/influxdb/values.yaml /root/files/k8s/influxdb/"

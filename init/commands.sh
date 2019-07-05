#!/bin/bash

helmInit="helm init; \
  kubectl --namespace kube-system create serviceaccount tiller; \
  kubectl create clusterrolebinding tiller-cluster-rule --clusterrole=cluster-admin --serviceaccount=kube-system:tiller; \
  sleep 10; \
  kubectl --namespace kube-system patch deploy tiller-deploy -p '{\"spec\":{\"template\":{\"spec\":{\"serviceAccount\":\"tiller\"}}}}';\
  kubectl create clusterrolebinding cluster-admin-binding --clusterrole=cluster-admin --user=$(gcloud config get-value account); \
  sleep 10; \
  helm repo update;"

helmGrafana="helm install -n bagan-grafana -f k8s/grafana/values.yaml k8s/grafana/;"
helmInflux="helm install -name bagan-influxdb -f k8s/influxdb/values.yaml k8s/influxdb/;"

handleServices="kubectl delete service grafana-bagan;
                    kubectl expose deployment grafana-bagan --type=LoadBalancer;
                    sleep 20;
                    kubectl port-forward \$(kubectl get pods -l app=bagan-influxdb -o jsonpath='{ .items[0].metadata.name }') 8086:8086 &
                 "

executePods="echo 1;cd /usr/share/sdkman/bin;
source sdkman-init.sh ;
cd /root;
echo 'Creating Pods';
kscript /usr/local/creator/ExperimentCoordinator.kt;"


clean="helm del --purge \$( kubectl get pods -l type=experiment -o custom-columns=:metadata.name --field-selector=status.phase=Running)"


loga="gcloud components install kubectl gcloud container clusters get-credentials $cluster --zone $zone;"

# \n
#gcloud auth configure-docker \n
#gcloud container clusters create $clux --zone $zonex --machine-type=$machine; $loga"
#printf '%s\n' "888"

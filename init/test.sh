#!/bin/sh

# include parse_yaml function
. validate_json.sh

# read yaml file


clux=""
if [ -z "$clusterName" ] | [ "$clusterName" = "null" ]
then
   clux="bagancluster"
else
   clux=$clusterName
fi

if [ -z "$zone" ] | [ "$zone" = "null" ]
then
   zonex="us-central1-a"
else
   zonex=$zone
fi


helmInit="helm init; \
  kubectl --namespace kube-system create serviceaccount tiller; \
  kubectl create clusterrolebinding tiller-cluster-rule --clusterrole=cluster-admin --serviceaccount=kube-system:tiller; \
  sleep 10; \
  kubectl --namespace kube-system patch deploy tiller-deploy -p '{\"spec\":{\"template\":{\"spec\":{\"serviceAccount\":\"tiller\"}}}}';\
  kubectl create clusterrolebinding cluster-admin-binding --clusterrole=cluster-admin --user=$(gcloud config get-value account); \
  sleep 10; \
  helm repo update;"

helmGrafana="helm install stable/grafana \
     --name grafana-bagan \
     --set datasources.\"datasources\.yaml\".apiVersion=1 \
     --set datasources.\"datasources\.yaml\".datasources[0].name=influxdb \
     --set datasources.\"datasources\.yaml\".datasources[0].type=influxdb \
     --set datasources.\"datasources\.yaml\".datasources[0].url=http://bagan-influxdb.default:8086 \
     --set datasources.\"datasources\.yaml\".datasources[0].access=proxy \
     --set datasources.\"datasources\.yaml\".datasources[0].database=tracking \
     --set datasources.\"datasources\.yaml\".datasources[0].isDefault=true;"

helmInflux="helm install --name bagan -f /usr/local/values.yaml stable/influxdb;"

handleServices="kubectl delete service grafana-bagan;
                  kubectl expose deployment grafana-bagan --type=LoadBalancer;
                  sleep 20;
                  kubectl port-forward \$(kubectl get pods -l app=bagan-influxdb -o jsonpath='{ .items[0].metadata.name }') 8086:8086 &
               "
#
 executePods="cd /usr/share/sdkman/bin;
 source sdkman-init.sh ;
 cd /root;
 echo 'Creating Pods';
 kscript /usr/local/creator/ExperimentCoordinator.kt;"
#

loga="gcloud components install kubectl;
gcloud container clusters get-credentials bagan --zone us-central1-a;"

 initGcloud="gcloud init;
 gcloud auth configure-docker;
 gcloud container clusters create $clux --zone $zonex --machine-type=n1-standard-8;
 $loga"



#echo $execution

#if [ "$type" = "gcloud" ]; then

   docker run -ti -v $HOME/.config/gcloud:/root/.config/gcloud  \
                  -v /Users/inaki/navetes/init/sa.json:/root/files/sa.json     cdsap/initbagan  /bin/bash -c "
                  $loga;
                  echo 'Grafana';
                  $executePods
                  echo 'Influxdb';
                  kubectl get pods;

                  kubectl get pod;
                  echo 'fin';"

#fi

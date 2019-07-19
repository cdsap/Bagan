#!/bin/sh

function endGcloud(){
   grafana_service=$(kubectl get services bagan-grafana  -o custom-columns=:status.loadBalancer.ingress[0].ip)
   cluster_ip=kubectl cluster-info | grep 'Kubernetes master' | awk '/http/ {print $NF}'
   sleep 10
   echo "Bagan setup finished"
   echo "You can check the reports in http://$grafana_service:3000 (admin/admin);"
   echo "Cluster url: $cluster_ip"
}

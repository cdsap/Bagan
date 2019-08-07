#!/bin/sh

function endGcloud(){
   sleep 10
   grafana_service="$(kubectl get services bagan-grafana  -o custom-columns=:status.loadBalancer.ingress[0].ip)"
   echo "Bagan setup finished"
   echo "Folder tmp/ contains the Experiments Charts executed in $type setup "
   echo "Grafana Dashboard generated:$grafana_service:3000/IS3q0sSWz (admin/admin)"
   echo "In case url is not shown, try in few seconds with: kubectl get services bagan-grafana  -o custom-columns=:status.loadBalancer.ingress[0].ip"
}

function endGcloudDocker(){
   sleep 10
   grafana_service="$(kubectl get services bagan-grafana  -o custom-columns=:status.loadBalancer.ingress[0].ip)"
   echo "Bagan setup finished \n Folder tmp/ contains the Experiments Charts executed in $type setup \n Folder tmp/ contains the Experiments Charts executed in $type setup"
}

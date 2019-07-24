#!/bin/sh

function endGcloud(){
   grafana_service="$(kubectl get services bagan-grafana  -o custom-columns=:status.loadBalancer.ingress[0].ip)"
   sleep 10
   echo "Bagan setup finished"
   echo "Folder tmp/ contains the Charts executed in $type setup"
   echo "You can check the reports in http://$grafana_service:3000/IS3q0sSWz (admin/admin);"
}

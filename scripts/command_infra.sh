#!/bin/bash

# Commands to execute the instalation of the Infra in Bagan(Influx+Grafana) in Bagan.
# Used by gcloud, gcloud_docker and standalone.
PATH_CHARTS="tmp"
helm_grafana="helm install -n bagan-grafana -f $PATH_CHARTS/grafana/values.yaml $PATH_CHARTS/grafana/"
helm_influx="helm install -n bagan-influxdb -f $PATH_CHARTS/influxdb/values.yaml $PATH_CHARTS/influxdb/"
kubectl_bagan_service_grafana_remove="kubectl delete service bagan-grafana"
kubectl_bagan_service_grafana_insert="kubectl expose deployment bagan-grafana --type=LoadBalancer"

function infraPods(){
  echo "$helm_grafana;"
  echo "$kubectl_bagan_service_grafana_remove;"
  echo "$kubectl_bagan_service_grafana_insert;"
  echo "$helm_influx;"
}

function grafana() {
  echo "$helm_grafana;"
}

function influxdb() {
  echo "$helm_influx;"
}

function services() {
  echo "$kubectl_bagan_service_grafana_remove;"
  echo "$kubectl_bagan_service_grafana_insert;"
}

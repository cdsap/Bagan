#!/bin/sh

# Commands to execute the Helm requirements in a cluster. It contains the
# cluster role bindings required by the cluster(gcloud).
# Used by modes gcloud, gcloud_docker and standalone.

sleep10="sleep 10"
sleep20="sleep 20"
helm_repo_update="helm repo update"

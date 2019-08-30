#!/bin/bash

# Commands to execute the creation of the secret credentials on Git.
# Used by mode gcloud, gcloud_docker and standalone.

function createSecret() {
  if [ "$private" == "true" ]; then
    create_secret="kubectl create secret generic git-creds  --from-file=ssh=$ssh   --from-file=known_hosts=$known_hosts"
    echo $create_secret
  fi
}

function createSecretDockerContainer(){
  if [ "$private" == "true" ]; then
    create_secret="kubectl create secret generic git-creds  --from-file=ssh=/root/.ssh/id_rsa   --from-file=known_hosts=/root/.ssh/known_hosts"
    echo "$create_secret;"
  fi
}

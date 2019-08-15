#!/bin/sh

function createSecret() {
  if [ "$private" == "true" ]; then
    create_secret="kubectl create secret generic git-creds  --from-file=ssh=$HOME/.ssh/id_rsa   --from-file=known_hosts=/tmp/known_hosts"
    echo $create_secret
  fi  
}

#!/bin/sh

function checkPreviousExperiments() {
  echo "Checking previous Experiments"

  experiments=$(helm ls --all experimen* --short)
  echo $experiments
  if [ -z "$experiments" ]; then
    echo "no previous experiments"
  else
     eval "$(removeExperiments)"
     eval "sleep 20"
  fi
}

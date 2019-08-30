#!/bin/bash

color() {
      printf '\033[%sm%s\033[m\n' "$@"
}

function log() {
    printf '%s\n' "$1"
}

function error() {
  color '31;1' $1
}

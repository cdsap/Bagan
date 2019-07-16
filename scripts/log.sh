#!/bin/sh

function log() {
  if [ "$debug" ]; then
    printf '%s\n' "[LOG]: $1"
  fi
}

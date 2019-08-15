#!/bin/sh



function log() {
    printf '%s\n' "$1"
}

function error() {

  color '31;1' $1
}

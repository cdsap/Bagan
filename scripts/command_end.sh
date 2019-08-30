#!/bin/bash

# Commands to show ending message on the execution of Bagan.
# Used by modes gcloud, gcloud_docker and standalone.

function endMessage(){
   messageEnd1="log \"\""
   messageEnd2="color '32;1' \"Bagan finished\""
   messageEnd3="log \"Folder tmp/ contains the Experiments Charts executed in $mode environment with command $command\""
   messageEnd4="log \"Dashboard with the results is avaible at: IP:3000/d/IS3q0sSWz\""
   messageEnd5="log \"To check the IP execute: ./bagan $mode grafana_dashboard\""
   messageEnd6="log \"To remove previous experiments: ./bagan $mode remove_experiments\""
   echo $messageEnd1
   echo $messageEnd2
   echo $messageEnd3
   echo $messageEnd4
   echo $messageEnd5
   echo $messageEnd6
}

function endMessageSingleCommand(){
   messageEnd="color '32;1' \"Bagan finished\""
   echo $messageEnd
}

function endMessageDocker(){
  message1="echo \"Bagan finished\""
  message2="echo \"Folder tmp/ contains the Experiments Charts executed in $mode environment with command $command\""
  message3="echo \"Dashboard with the results is avaible at: IP:3000/d/IS3q0sSWz\""
  message4="echo \"To check the IP execute: ./bagan $mode grafana_dashboard\""
  message5="echo \"To remove previous experiments: ./bagan $mode remove_experiments\""
  echo $message1
  echo $message2
  echo $message3
  echo $message4
  echo $message5
}

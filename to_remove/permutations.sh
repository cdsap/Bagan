#/bin/bash
#   "properties": [
#      {
#         "name": "org.gradle.jvmargs",
#         "options": ["-Xmx3g","-Xmx4g"]
#      },
#      {
#         "name": "org.gradle.workers.max",
#         "options": ["4","6"]
#      }
#   ]

FILE="bagan_conf.json"
propertiesJson=$(cat $FILE | jq -c -r '.bagan.experiments.properties .[]' | tr -d '\r')



for i in $(jq -r ".bagan.experiments.properties" bagan_conf.json)
do
  echo $i
  echo 222
done

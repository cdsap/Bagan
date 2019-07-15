#/bin/bash

a="./gradlew command1 command2"
read -ra ADDR <<< "$a"
commands=()
gradleWrapperPresent="false"
index=0
for i in "${ADDR[@]}"; do
    if [ "$i" == "./gradlew" ]; then
       gradleWrapperPresent="true"
    fi
    if [ "$i" != "clean" ] && [ "$i" != "./gradlew" ]; then
        commands[$index]=$i
        ((index++))
    fi
done

if [ "$gradleWrapperPresent" == "false" ]; then
  printf '%s\n' "Error: ./gradlew was not present in the gradleCommand attribute"
  exit 1
fi

for j in "${commands[@]}"; do
     echo $j
done

#generate json file for each grafana
## for each command create a new dashboard

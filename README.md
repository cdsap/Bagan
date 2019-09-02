# Bagan

![alt text](resources/bagan_1.png "Bagan image")


Bagan is a framework to automate and parallelize the execution, reporting and collection of data with different types of experiments in Gradle projects using Kubernetes.

1- How to use Bagan
2- bagan_conf.json
3- Modes
4- Commands
5- Internals Bagan
6- Lifecycle Bagan
6- Money impact on Gcloud
7- Examples
7- Deploying Bagan


## How to use
Once you have downloaded this repository you will need to set up the `bagan_conf.json`. There ypu can setup different properties
like the type of experiments you want to apply, the target repository or the resources you want to use in the Kubernetes environments.

Bagan will be executed with the `./bagan` command following the next format:

` ./bagan MODE COMMAND`

Bagan will execute the experiments in a Kubernetes environments. There using the package Manager will be created the infrastrucure applications
like Grafaa and InfluxDb and the experimentation. To report the data Bagan will use Talaiot. Bagan is working


Bagan will deploy a new cluster or will use an existing cluster to execute the experimentationw defined
in the main conf file.
Bagan will use Grafana and

## bagan_conf.json

| Property          |      Description                                                          |
|-------------------|---------------------------------------------------------------------------|
| repository        | Repository of the project we want to experiment                           |
| gradleCommand     | Gradle command to be executed in each Experiment from the repository                          |
| cluesterName      | Name of the cluster which the experimentation will be exccited. If is not specified and the command implies the creation the default name is Bagan, modes gcloud, gcloud_docker |
| machine           | Type of Machine used bu the exeperimentation in modes gcloud, gcloud_docker. Default snmachines-1. Check cost of Bagan |
| private           | Flag to indicate the experimentation will be executed in a private repository |
| ssh               | Pah to the rsa id key for the private reposutory   |
| known_hosts       | Path to the known_hosts file requiered to create the secret onf the pods   |
| iterations        | Number of executions of the gradle command indicate in the configuration   |
| experiments       | Experimentation properties for the execution,, see next secion.   |

Example:
```
{
   "bagan": {
      "repository": "git@github.com:cdsap/TestPrivateRepository.git",
      "gradleCommand": "./gradlew clean assembleDebug",
      "clusterName": "",
      "machine": "n1-standard-4",
      "private": true,
      "ssh": "/Users/inaki/.ssh/id_rsa",
      "known_hosts": "/Users/inaki/.ssh/known_hosts",
      "iterations": 20,
      "experiments": {
         "properties": [
            {
               "name": "org.gradle.jvmargs",
               "options": ["-Xmx3g","-Xmx4g"]
            }
         ],
         "branch": [ "develop3","master"]
      }
   }
}

```

#### Type Experoments

Experiments are the type of different entites will be applied on each experrimen. You need to
use at least one the experiments to execute Bagan. Currently thetypes of experiments supported are:

| Experiment             |      Description                                                          |
|------------------------|---------------------------------------------------------------------------|
| properties             | create_cluster + credentials + helm + infra_pods + experiments                                           |
| branch                 | Create infrastructure and execute experiments in the environment mode selected                          |
| gradleWrapperVersion   | Execute experiments in the environment mode selected   |

Example Pe
```
"experiments": {
   "properties": [
      {
         "name": "org.gradle.jvmargs",
         "options": ["-Xmx3g","-Xmx4g"]
      },
      {
         "name": "org.gradle.caching",
         "options": ["true","false"]
      },
   ],
   "branch": [ "develop","master"],
   "gradleWrapperVersion" : ["5.5", 5.4"]
}
```
Bagan will apply a cartesiann product for all type experiments and each combination will be consider
as element to be testd.
Following the example, we will 16 different combinations:

|sss                         |dsd    | | |
|----------------------------|-------|---|------------------|
| org.gradle.jvmargs="-Xmx3g"  <br> org.gradle.caching="true" <br> develop <br> 5.5 | org.gradle.jvmargs="-Xmx3g"  <br> org.gradle.caching="true" <br> develop <br> 5.4 | org.gradle.jvmargs="-Xmx3g"  <br> org.gradle.caching="true" <br> master <br> 5.4 | org.gradle.jvmargs="-Xmx3g"  <br> org.gradle.caching="true" <br> master <br> 5.5 |
| org.gradle.jvmargs="-Xmx4g"  <br> org.gradle.caching="true" <br> develop <br> 5.5 | org.gradle.jvmargs="-Xmx4g"  <br> org.gradle.caching="true" <br> develop <br> 5.4 | org.gradle.jvmargs="-Xmx4g"  <br> org.gradle.caching="true" <br> master <br> 5.4 | org.gradle.jvmargs="-Xmx4g"  <br> org.gradle.caching="true" <br> master <br> 5.5 |
| org.gradle.jvmargs="-Xmx3g"  <br> org.gradle.caching="false" <br> develop <br> 5.5 | org.gradle.jvmargs="-Xmx3g"  <br> org.gradle.caching="false" <br> develop <br> 5.4 | org.gradle.jvmargs="-Xmx3g"  <br> org.gradle.caching="false" <br> master <br> 5.4 | org.gradle.jvmargs="-Xmx3g"  <br> org.gradle.caching="false" <br> master <br> 5.5 |
| org.gradle.jvmargs="-Xmx4g"  <br> org.gradle.caching="false" <br> develop <br> 5.5 | org.gradle.jvmargs="-Xmx4g"  <br> org.gradle.caching="false" <br> develop <br> 5.4 | org.gradle.jvmargs="-Xmx4g"  <br> org.gradle.caching="false" <br> master <br> 5.4 | org.gradle.jvmargs="-Xmx4g"  <br> org.gradle.caching="false" <br> master <br> 5.5 |




### Modes
Mode is the K8s environment where you want to execute the experimentation
There are supported thre modes

| Mode           |      Description                                                          |
|----------------|---------------------------------------------------------------------------|
| gcloud         | Mode for logging (Silent,Info)                                            |
| gcloud_docker  | Configuration to ignore the execution of Talaiot                          |
| standalone     | Generation of unique identifier for each execution(disabled by default)   |

If you have actually your cluster created in Gcloud or you have a host machine where you want
to execute Bagan
Gcloud_docker will be used to enapcuslate the exevution of the gcloud encapsulated in a docker image

### Commands
Commands are the tasks to be executed in the mode selected. There are two main commands groups.

#### Meta Commmands
Used to execute a sequence of commands, these commands execute all the required steps to finish with
the experimentation on Kubernetes

| Mode           |      Description                                                          |
|----------------|---------------------------------------------------------------------------|
| cluster        | Create cluster, infrastructure and execute experiments in the environment mode selected.                                            |
| infrastrcture  | Create infrastructure and execute experiments in the environment mode selected                          |
| experiment     | Execute experiments in the environment mode selected   |

Examples:
```
  ./bagan gcloud cluster
  ./bagan gcloud_docker infrastructure
  ./bagan standalone experiments
```
#### Single commands
Meta commands are composed by single commands. You can execute single commands depending on your requeriments.
For example if you are using an existing cluster and some of the components required by Bagan are installed you can execute a single command:

| Mode           |      Description                                                          |
|----------------|---------------------------------------------------------------------------|
| create_cluster        | Create cluster, infrastructure and execute experiments in the environment mode selected.                                            |
| infra_pods  | Create infrastructure and execute experiments in the environment mode selected                          |
| credentials     | Execute experiments in the environment mode selected   |
| secret     | Execute experiments in the environment mode selected   |
| helm     | Execute experiments in the environment mode selected   |
| helm_init     | Execute experiments in the environment mode selected   |
| helm_clusterrolebinding     | Execute experiments in the environment mode selected   |
| grafana     | Execute experiments in the environment mode selected   |
| influxfb     | Execute experiments in the environment mode selected   |
| services     | Execute experiments in the environment mode selected   |
| remove_experiments     | Execute experiments in the environment mode selected   |
| grafana_dashboard    | Execute experiments in the environment mode selected   |


So on the same way we can see how the Meta Command are composed:

| Mode           |      Description                                                          |
|----------------|---------------------------------------------------------------------------|
| cluster        | create_cluster + credentials + helm + infra_pods + experiments                                           |
| infrastrcture  | Create infrastructure and execute experiments in the environment mode selected                          |
| experiment     | Execute experiments in the environment mode selected   |



## Internals Bagan
Bagan is composed by different tools like Helm or Talaiot. One of the m
Bagan is composed by three main phases:


## Lifecycle Bagan
* Verificiation
* Provisioming environment mode and command
* Execution Experiments




BaganExecutor
Bagan was born with the idea to provide a integral solution when you want to compare different properties/branches in your Gadle project. Bagan use Kubernets in different environments to build the infrastrcture required to cocllect the information. Currently the environments supported are:
Gcloud
Custom envornments

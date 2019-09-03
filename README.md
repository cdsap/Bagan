# Bagan

![alt text](resources/bagan_1.png "Bagan image")


Bagan is a framework that helps to automate the execution, reporting and collection of data with different types of experiments in Gradle projects using Kubernetes.

# Table of Contents
1. [How to use Bagan](#how-to-use-bagan)
2. [bagan_conf.json](#bagan_conf.json)
3. [Experiments](#experiments)
4. [Modes](#modes)
5. [Commands](#commands)
6. [Requeriments Execution](#requeriments)
7. [Lifecycle Bagan](#lifecycle)
8. [Internals Bagan](#internals)
9. [The cost of Bagan](#cost)
10. [Examples](#examples)
11. [Deploying Bagan](#deploy)
12. [Constribute](#contribute)


## How to use Bagan <a name="how-to-use-bagan"></a>
Once you have downloaded this repository you need to set up the `bagan_conf.json`. There you can setup different properties
like the type of experiments you want to apply, the target repository or the resources you want to use in the Kubernetes environments.

Bagan will be executed with the `./bagan` command following the next format:

` ./bagan MODE COMMAND`

Bagan will execute the experiments in a Kubernetes environment. For each experiment, Bagan will create a Helm Release where it will run the target repository applying the experimentation.
To report the information of the build Bagan will inject Talaiot in the Gradle configuration of the project using InfluxDb as time-series database and Grafana as dashboard visualization tool.

Bagan can deploy a new cluster or will use an existing cluster to execute the experimentation defined
in the main conf file.


## bagan_conf.json <a name="bagan_conf.json"></a>

| Property          |      Description                                                          |
|-------------------|---------------------------------------------------------------------------|
| repository        | Repository of the project we want to experiment                           |
| gradleCommand     | Gradle command to be executed in each Experiment from the repository                          |
| cluesterName      | Name of the cluster which the experimentation will be executed. If is not specified and the command implies the creation the default name is Bagan, modes gcloud, gcloud_docker |
| machine           | Type of Machine used bu the experimentation in modes gcloud, gcloud_docker. Default snmachines-1. Check cost of Bagan |
| private           | Flag to indicate the experimentation will be executed in a private repository |
| ssh               | Pah to the rsa id key for the private repository   |
| known_hosts       | Path to the known_hosts file required to create the secret onf the pods   |
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

## Experiments  <a name="experiments"></a>

Experiments are the type of different entities will be applied on each experiment. You need to
use at least one the experiments to execute Bagan. Currently the types of experiments supported are:

| Experiment             |      Description                                                          |
|------------------------|---------------------------------------------------------------------------|
| properties             | create_cluster + credentials + helm + infra_pods + experiments                                           |
| branch                 | Create infrastructure and execute experiments in the environment mode selected                          |
| gradleWrapperVersion   | Execute experiments in the environment mode selected   |

Example Pe
```
"experiments": {
   "properties": [],
   "branch": [],
   "gradleWrapperVersion": []
}
```

Bagan will apply a cartesian product for all type experiments and each combination will be consider
as element to be test.
In case we want to experiment with `jvmargs` in the project with will include.
```
"experiments": {
   "properties": [
      {
         "name": "org.gradle.jvmargs",
         "options": ["-Xmx3g","-Xmx4g"]
      }
   ]
}
```

|Experiments                 |
|----------------------------|
|org.gradle.jvmargs="-Xmx3g" |
|org.gradle.jvmargs="-Xmx4g" |


We can experiment with different gradle properties at the same time and with the other type of experiments like 

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


|Experiments                         |    | | |
|----------------------------|-------|---|------------------|
| org.gradle.jvmargs="-Xmx3g"  <br> org.gradle.caching="true" <br> develop <br> 5.5 | org.gradle.jvmargs="-Xmx3g"  <br> org.gradle.caching="true" <br> develop <br> 5.4 | org.gradle.jvmargs="-Xmx3g"  <br> org.gradle.caching="true" <br> master <br> 5.4 | org.gradle.jvmargs="-Xmx3g"  <br> org.gradle.caching="true" <br> master <br> 5.5 |
| org.gradle.jvmargs="-Xmx4g"  <br> org.gradle.caching="true" <br> develop <br> 5.5 | org.gradle.jvmargs="-Xmx4g"  <br> org.gradle.caching="true" <br> develop <br> 5.4 | org.gradle.jvmargs="-Xmx4g"  <br> org.gradle.caching="true" <br> master <br> 5.4 | org.gradle.jvmargs="-Xmx4g"  <br> org.gradle.caching="true" <br> master <br> 5.5 |
| org.gradle.jvmargs="-Xmx3g"  <br> org.gradle.caching="false" <br> develop <br> 5.5 | org.gradle.jvmargs="-Xmx3g"  <br> org.gradle.caching="false" <br> develop <br> 5.4 | org.gradle.jvmargs="-Xmx3g"  <br> org.gradle.caching="false" <br> master <br> 5.4 | org.gradle.jvmargs="-Xmx3g"  <br> org.gradle.caching="false" <br> master <br> 5.5 |
| org.gradle.jvmargs="-Xmx4g"  <br> org.gradle.caching="false" <br> develop <br> 5.5 | org.gradle.jvmargs="-Xmx4g"  <br> org.gradle.caching="false" <br> develop <br> 5.4 | org.gradle.jvmargs="-Xmx4g"  <br> org.gradle.caching="false" <br> master <br> 5.4 | org.gradle.jvmargs="-Xmx4g"  <br> org.gradle.caching="false" <br> master <br> 5.5 |

In this example there are 16 different combinations. Bagan will create 16 different pods with the specific configuration. Check the cost of Bagan section 
to understand better the impact in terms of cost of high perumations experiments.


### Modes  <a name="modes"></a>
Mode is the K8s environment where you want to execute the experimentation. Modes are needed to provision and interact with the
Kubernetes environment
There are supported thre modes

| Mode           |      Description                                                          |
|----------------|---------------------------------------------------------------------------|
| gcloud         | Bagan will be executed in Kubernetes Engine in Gcloud using gcloud sdk     |
| gcloud_docker  | Bagan will be executed in Kubernetes Engine in Gcloud using Docker and avoiding to set up different configurations in your machine. The image is cdsap/bagan-init |
| standalone     | Bagan will be executed in the enviroement configured in the host machine.   |

If you have actually your cluster created in Gcloud or you have a host machine where you want
to execute Bagan
Gcloud_docker will be used to encapsulate the execution of the gcloud encapsulated in a docker image

### Commands  <a name="commands"></a>
Commands are the tasks to be executed in the mode selected. There are two main commands groups.

#### Meta Commmands
Used to execute a sequence of commands, these commands execute all the required steps to finish with
the experimentation on Kubernetes

| Mode           |      Description                                                          |
|----------------|---------------------------------------------------------------------------|
| cluster        | Create cluster, infrastructure and execute experiments in the environment mode selected.                                            |
| infrastructure | Create infrastructure and execute experiments in the environment mode selected                          |
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

| Command                     |      Description                                                                                       |
|-----------------------------|--------------------------------------------------------------------------------------------------------|
| create_cluster              | Create cluster, infrastructure and execute experiments in the environment mode selected.               |
| infra_pods                  | Create infrastructure and execute experiments in the environment mode selected   (grafana + influxdb)  |
| credentials                 | Get the credentials for the current cluster                                                            |
| secret                      | Create secret object in the mode selected require to experiment with private repositories              |
| helm                        | Execute initialization of helm and the cluster role binding required in Kubernetes                     |
| helm_init                   | Execute initialization of Helm                                                                         |
| helm_clusterrolebinding     | Create  the cluster role binding required in Kubernetes                                                |
| grafana                     | Install the Chart of Grafana with Helm at the cluster                                                  |
| influxfb                    | Install the Chart of InfluxDb with Helm at the cluster                                                 |
| services                    | Creates a service port type Load Balance                                                               |
| remove_experiments          | Remove experiments in the cluster                                                                      |
| grafana_dashboard           | Retrieve the plublic IP of the Dashboard created                                                       |


So on the same way we can see how the Meta Command are composed:

| Command           |      Single Commands                                                          |
|-------------------|---------------------------------------------------------------------------|
| cluster           | create_cluster + credentials + helm + infra_pods + experiments                                           |
| infrastrcture     | Create infrastructure and execute experiments in the environment mode selected                          |
| experiment        | credentials + experiments   |


    
## Lifecycle Bagan  <a name="lifecycle"></a>
We can group the execution of Bagan in three main stages:

* Verificiation
* Provisioning
* Execution Experiments:


![alt text](resources/lifecycle.png "Life Cycle")


### Verification 
During this phase Bagan will validate the configuration file `bagan_conf.json` and the inputs included. It's executed in the host machine 
and requires jq to perform the validation.  

### Provisioning 
In this phase, Bagan will execute the command included in the mode selected. 
For `gcloud`and `standalone` modes it will be executed in the host machine. For `gcloud_docker` will be executed in a docker image(cdsap/bagan-init).
Kubectl and Helm will be configured.
Depending on the command selected different actions will be handle. Check commands section.


### Execution 
The execution phase will be driven by the kscript `BaganGenerator.kt`. This script  has two main functions:
* Calculate the combination of experiments
* Generate the Grafana Dashboard with the experimentation and Gradle Commands
* Create and install the experiment environment.

For each experiment `BaganGenerator.kt` will create: 
* Chart.yaml
* Values.yaml
* templates/configmapexpermientN.yaml
* templates/podexperimentN.yaml

Finally with Helm will install the package in the selected environment.

Once the experiments are running you can check the Grafana dashboard to visualize the results:
```
http://IP:3000/d/IS3q0sSWz
```

The `IP` corresponds with the public IP exposed as Service type load balancer on the Grafana deployment. 
To check the IP you can execute :
```
./bagan gcloud grafana_dashboard
```



## Internals Bagan  <a name="internals"></a>

### Kubernetes Infrastructure

The overall picture of Bagan from the perspective of Kubernetes will be:

![alt text](resources/kubernetes_infra.png "Kubernetes infra")

Once the experiments are generated and installed one Pod will be linked with the configuration, it will perform the build with the `gradleCommand` and `iterations` defined in 
the `bagan_conf.json`.  



### Pod execution 
Every experiment is linked with one release generated by the `BaganGenerator.kt`. 
Wnen Helm creates the pod first will download the repository 


![alt text](resources/pod.png "Pod")


### Gcloud console 



## The cost of Bagan  <a name="cost"></a>
In case you are using Google Cloud as Kubernetes environment you should consider the impact in terms of momey of the selection 
of the different elements.
Resources are limited and  constraint by the type of the machine you have selected. Android projects are expensive in terms of 
memory consumption and create multiples combinations will cause the fail of the experiments:


![alt text](resources/no_resources.png "No Resources")

On the other side if you consider to increase the resources of the machine we should consider the cost. Google Cloud publish the 
cost for the different types of machines:

![alt text](resources/cost.png "Cost")

Bagan doesn't put any restriction on the machine selected, but you should consider the impact of the experimentation. 
Once the experiments are succeded and you don't want to repeat experiments and the data is done you should remove the cluster in 
case you are using personal project.

Another point to consider is depending on the machine you have selected, maybe is not available in the zone you have selected. 
The default zone is `us-west1-a` but you can choose other depending on your requirements. 
Check more documentation about region, zones and availability in Gcloud here:
https://cloud.google.com/compute/docs/regions-zones/#available

## Examples  <a name="examples"></a>

### Example 1

Simple example of private repository generated by Android Studio

| bagan_conf      |                                                              |
|-----------------|--------------------------------------------------------------|
| machine         |n2-standard-2                                                 |
| zone            |asia-southeast1-b                                             |
| repository      |git@github.com:cdsap/TestPrivateRepository.git                |
| private         |true                                                          |
| experiments     |gradle properties :{ "org.gradle.jvmargs": ["-Xmx3g","-Xmx4g"]|
| command         | ./gradlew clean assembleDebug                                |
| iterations      |20                                                            |


Result:

![alt text](resources/experiment1.png "experiment1")


## Deploy  <a name="deploy"></a>
Bagan is open source and you can update create your requirements. 
This is the structure of the project:

| Folder               |                                                                |
|----------------------|---------------------------------------------------------------------------|
| baganGenerator       | Kotlin project with the managment of Create infrastructure and execute experiments in the environment mode selected                          |
| deploy               | Deployment script of docker images adding the build output generated by the Bagan Generator                                        |
| docker               | Docker images for the installation with docker  and execution of the Pods                      |
| k8s                  | Packages Charts for Grafana and InfluxDb  |
| scripts               |  Bash scripts for validation and provisioning of the environment  |

### Bagan Generator
Is the Kotlin project 

Additionaly it creates a custom task to override the headers and required to execute kscript on it. 

### Deploy 
If you want to use your own images you can setup in the deployment script the required files. 

* Build the Bagan Generator
* Copy the binaries required for each image to the folder
* Build and push the docker image for bagan-init
* Build and push the docker image for bagan-pod 

### Docker 
Contains the Docker iamges for bagan-init and bagan-pod. It includes the binaries genereated by the baganGenerator in the 
deployment step.
In case you want to provide your own images you can set the values on the deployment file:

```
VERSION="0.1.6"
REGISTRY=""
IMAGE_BAGAN_INIT="cdsap/bagan-init"
IMAGE_BAGAN_POD="cdsap/bagan-pod-injector"
```

### K8s
Contains the Grafana and InfluxDb charts.
Grafana contains the provisioned datasource configuration for InfluxDb. Also includes the default dashboard that will be updated when experimenteation will be execited

### Scripts
It contains the bash scripts related to the verificaction and provisioning phase. 
For eaach mode there is availanle one command executor where the scripts can be done. 

## Contribute

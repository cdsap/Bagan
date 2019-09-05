# Bagan

![alt text](resources/bagan_1.png "Bagan image")


Bagan is an experimental framework used to automate the execution, reporting and collection of data for Gradle projects, using Kubernetes.

![alt text](resources/experiment2.png "Bagan image")

# Table of Contents
1. [How to use Bagan](#how-to-use-bagan)
2. [bagan_conf.json](#bagan_conf.json)
3. [Experiments](#experiments)
4. [Modes](#modes)
5. [Commands](#commands)
6. [Lifecycle Bagan](#lifecycle)
7. [Internals Bagan](#internals)
8. [The cost of Bagan](#cost)
9. [Examples](#examples)
10. [Deploying Bagan](#deploy)
11. [Tools/Libraries used in Bagan](#tools)
12. [The real Bagan](#bagan)


## How to use Bagan <a name="how-to-use-bagan"></a>
Once you have downloaded this repository you need to set up the `bagan_conf.json`. You can define different properties
like the type of experiments you want to apply, the target repository or the resources you want to use in the Kubernetes environments.

Bagan will be executed with the `./bagan` command following the next format:

` ./bagan MODE COMMAND`

The execution framework and reporting happen in Kubernetes environment. For each experiment, Bagan will create a Helm Release where it will run the target repository applying the experimentation.
To report and collect the information of the build, Bagan will inject Talaiot in the Gradle configuration of the project using InfluxDb as time-series database and Grafana as a dashboard visualization tool.

Bagan can deploy a new cluster or will use an existing cluster to execute the experimentation defined
in the main conf file.


## bagan_conf.json <a name="bagan_conf.json"></a>

The main configuration file is `bagan_conf.json`. The properties to be configured are:

| Property          |      Description                                                                                                  |
|-------------------|-------------------------------------------------------------------------------------------------------------------|
| repository        | Repository of the project we want to experiment.                                                                  |
| gradleCommand     | Gradle command to be executed in each Experiment from the repository.                                             |
| clusterName       | Name of the cluster where the experimentation will be executed. If it is not specified the default name is Bagan. |
| zone              | Zone of the machine for Kubernetes Engine in Google Cloud.                                                        |
| machine           | Type of Machine used in the experimentation. Check [The cost of Bagan](#cost).                                    |
| private           | Flag to indicate the target repository is private.                                                                |
| ssh               | Pah to the ssh key.                                                                                               |
| known_hosts       | Path to the file that has the name of a server and a public-key that is applicable to the server machine.         |
| iterations        | Number of executions of the Gradle command.                                                                       |
| experiments       | Experimentation properties for the execution.                                                                     |

Example:
```
{
   "bagan": {
      "repository": "git@github.com:cdsap/TestPrivateRepository.git",
      "gradleCommand": "./gradlew clean assembleDebug",
      "clusterName": "",
      "machine": "n1-standard-4",
      "private": true,
      "ssh": "/user/.ssh/id_rsa",
      "known_hosts": "/user/.ssh/known_hosts",
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

An Experiment is an Entity that represents a specific state of the target repository. This state is related to a different configuration of the build system or the control version system. Currently, the types of experiments supported are:

| Experiment  Type       |      Description                                 |
|------------------------|--------------------------------------------------|
| properties             | Specifies different values for Gradle properties.|
| branch                 | Specifies different branches for experimentation.|
| gradleWrapperVersion   | Specifies different versions of the wrapper.     |

Example:
```
"experiments": {
   "properties": [],
   "branch": [],
   "gradleWrapperVersion": []
}
```

Bagan will apply a cartesian product for all the experiments, and each combination will be considered as an element to be tested.
In case we want to experiment with `jvmargs` in the project, we will include:
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
And the experiments generated are:

|Experiments                 |
|----------------------------|
|org.gradle.jvmargs="-Xmx3g" |
|org.gradle.jvmargs="-Xmx4g" |


We can experiment with different types of experiments, here we are using properties, branches and Gradle Wrapper versions:

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

The experiments generated are:

|Experiments                         |    | | |
|----------------------------|-------|---|------------------|
| org.gradle.jvmargs="-Xmx3g"  <br> org.gradle.caching="true" <br> develop <br> 5.5 | org.gradle.jvmargs="-Xmx3g"  <br> org.gradle.caching="true" <br> develop <br> 5.4 | org.gradle.jvmargs="-Xmx3g"  <br> org.gradle.caching="true" <br> master <br> 5.4 | org.gradle.jvmargs="-Xmx3g"  <br> org.gradle.caching="true" <br> master <br> 5.5 |
| org.gradle.jvmargs="-Xmx4g"  <br> org.gradle.caching="true" <br> develop <br> 5.5 | org.gradle.jvmargs="-Xmx4g"  <br> org.gradle.caching="true" <br> develop <br> 5.4 | org.gradle.jvmargs="-Xmx4g"  <br> org.gradle.caching="true" <br> master <br> 5.4 | org.gradle.jvmargs="-Xmx4g"  <br> org.gradle.caching="true" <br> master <br> 5.5 |
| org.gradle.jvmargs="-Xmx3g"  <br> org.gradle.caching="false" <br> develop <br> 5.5 | org.gradle.jvmargs="-Xmx3g"  <br> org.gradle.caching="false" <br> develop <br> 5.4 | org.gradle.jvmargs="-Xmx3g"  <br> org.gradle.caching="false" <br> master <br> 5.4 | org.gradle.jvmargs="-Xmx3g"  <br> org.gradle.caching="false" <br> master <br> 5.5 |
| org.gradle.jvmargs="-Xmx4g"  <br> org.gradle.caching="false" <br> develop <br> 5.5 | org.gradle.jvmargs="-Xmx4g"  <br> org.gradle.caching="false" <br> develop <br> 5.4 | org.gradle.jvmargs="-Xmx4g"  <br> org.gradle.caching="false" <br> master <br> 5.4 | org.gradle.jvmargs="-Xmx4g"  <br> org.gradle.caching="false" <br> master <br> 5.5 |



### Modes  <a name="modes"></a>
Bagan uses modes to identify the environment which Kubernetes will use to set up the configuration and execute the experiments.
There are supported three modes:

| Mode           |      Description                                                                                                                                          |
|----------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------|
| gcloud         | Bagan will be executed in Kubernetes Engine in Gcloud using gcloud sdk.                                                                                   |
| gcloud_docker  | Bagan will be executed in Kubernetes Engine in Gcloud using Docker and avoiding to install additional sdk in your machine. The image is cdsap/bagan-init. |
| standalone     | Bagan will be executed in the environment configured in the host machine.                                                                                 |


### Commands  <a name="commands"></a>
Commands are the tasks to be executed in the mode selected. There are two main commands groups.

#### Meta Commands
Meta Commands offer a complete execution of Bagan. It may contain multiple single commands.

| Command        |      Description                                                                        |
|----------------|-----------------------------------------------------------------------------------------|
| cluster        | Create cluster, infrastructure and execute experiments in the environment mode chosen.  |
| infrastructure | Create infrastructure and execute experiments in the environment mode chosen.           |
| experiment     | Execute experiments in the environment mode chosen.                                     |

Examples:
```
  ./bagan gcloud cluster
  ./bagan gcloud_docker infrastructure
  ./bagan standalone experiments
```
#### Single commands
Meta commands are composed by single commands. You can execute single commands depending on your requirements.
For example, if you are using an existing cluster and some of the components required by Bagan are installed, you can execute a single command:

| Command                     |      Description                                                                               |
|-----------------------------|------------------------------------------------------------------------------------------------|
| create_cluster              | Create a new cluster in the environment mode selected.                                         |
| infra_pods                  | Create infrastructure(Grafana + InfluxDb)  in the environment mode selected.                   |
| credentials                 | Get the credentials for the current cluster.                                                   |
| secret                      | Create a secret object in the mode selected, required to experiment with private repositories. |
| helm                        | Execute initialization of Helm and the cluster role binding required in Kubernetes.            |
| helm_init                   | Execute initialization of Helm.                                                                |
| helm_clusterrolebinding     | Create the cluster role binding required in Kubernetes.                                        |
| grafana                     | Install the Chart of Grafana with Helm at the cluster.                                         |
| influxfb                    | Install the Chart of InfluxDb with Helm at the cluster.                                        |
| services                    | Creates a service port of type Load Balance for Grafana.                                       |
| remove_experiments          | Remove experiments in the cluster.                                                             |
| grafana_dashboard           | Retrieve the public IP of the Dashboard created.                                               |


    
## Lifecycle Bagan  <a name="lifecycle"></a>
We can group the execution of Bagan in three main stages:

* Verification
* Provisioning
* Execution Experiments


![alt text](resources/lifecycle.png "Life Cycle")


### Verification 
During this phase, Bagan will validate the configuration file `bagan_conf.json` and the inputs included. It's executed in the host machine 
and requires [jq](https://stedolan.github.io/jq/) to perform the validation.  

### Provisioning 
In this phase, Bagan will execute the command included in the mode selected. 
For `gcloud` and `standalone` modes, it will be executed in the host machine. For `gcloud_docker` will be executed in a docker image(cdsap/bagan-init).
Kubectl and Helm will be configured.
Depending on the command selected, different actions will be handle. Check commands section.


### Execution 
The execution phase will be driven by the kscript `BaganGenerator.kt`. This script has three main functions:
* Calculate the combination of experiments
* Generate the Grafana Dashboard with the experimentation and Gradle Commands
* Create and install the experiment environment.

Before the execution of the experiments, the deployments of Grafana and InfluxDb are created. 
The release of Grafana includes a provisioned data source for InfluxDb. Later, a Dashboard is generated with the configuration provided by the `bagan_conf.json`:
 ![alt text](resources/experiment1.png "Bagan image")
 The dashboard contains five panels:
* Graph of the command group by experiment.
* Percentile(80) of the experiments on the command configured.
* Minimum build times by experiment and command.
* Experiment Winner.
* Information about the experiments. 

To access the dashboard, you have to access:

`http://IP:3000/d/IS3q0sSWz`

To retrieve the IP, you can use the Bagan command:

`./bagan gcloud grafana_dashboard`

User and password by default are admin/admin.
Additionally, you can create your panels in Grafana. It follows the same scheme used in Talaiot being the primary points build and task:



## Internals Bagan  <a name="internals"></a>

### Kubernetes Infrastructure

Independent of the environment selected, the general infrastructure for Bagan in Kubernetes is:

![alt text](resources/kubernetes_infra.png "Kubernetes infra")

To create the instances of Grafana and InfluxDb, we are using the Kubernetes package manager Helm. Helm is used to create experiments in Kubernetes too. 
In case of modes `gcloud` and `gcloud_docker`, additional Cluster Role Binding objects are created to be used by tiller/Helm.
For `gcloud` and `standalone`, we can use `kubectl` as a command-line interface for running commands against Kubernetes clusters.

We can use the Google cloud console, https://console.cloud.google.com, where we have a user interface to manage the cluster:

![alt text](resources/googlecloud.png "Google Cloud")


### Pod Experiment execution 
Every experiment generates a Helm release with this structure:

![alt text](resources/helmexperiment.png "Helm")

`values.yaml` contains the information related to the experiment and the configuration provided in the Bagan conf. 

```
repository: https://github.com/android/plaid.git
branch: master
configMaps: configmapexperiment1
pod: experiment1
session: sessionId
name: experiment1
image: cdsap/bagan-pod-injector:0.1.6
command: ./gradlew assemble
iterations: 10
```

`configmapexperimentN.yaml` contains the data of the permutation calculated for the experiment:
```
apiVersion: v1
kind: ConfigMap
metadata:
  name: {{ .Values.configMaps }}
  labels:
    app: bagan
    type: experiment
    session: {{ .Values.session }}
data:
  id: {{ .Values.name }}
  properties: |
               org.gradle.jvmargs=-Xmx6g
               org.gradle.workers.max=1
```

The execution of the build happens inside the pod created by `podexperimentN.yaml`. The Docker image used by the pod is `cdsap/bagan-pod`. 
This pod is responsible for:
* Fetch Target repository in a specific volume.
* Inject Talaiot in the project 
* Apply the experimentation for Gradle Properties and Gradle Wrapper versions parsing the data from the configmap.
* Execute the build given N iteration

The execution flow is:

![alt text](resources/pod.png "Pod")

When the Pod is running, it will execute the `ExperimentController.kt` (using kscript) that starts applying Talaiot in the main Gradle configuration file (groovy/kts):
```
publishers {
    influxDbPublisher {
        dbName = "tracking"
        url = "http://bagan-influxdb.default:8086"
        taskMetricName = "tasks"
        buildMetricName = "build"
    }
}
```

Later, the `ExperimentController.kt` will parse the data of the configmap and will apply the different experiments. Note that in the case of Branch Experimentation, the experiment will be applied in the Pod and not in the configmap.


### Requirements by Mode

`gcloud`
* [jq](https://stedolan.github.io/jq/)
* [Google Cloud SDK](https://cloud.google.com/sdk/)
* [kscript](https://github.com/holgerbrandl/kscript) 


`gcloud_docker` 
* [jq](https://stedolan.github.io/jq/)
* [Docker](https://www.docker.com/) 

`standalone` 
* [jq](https://stedolan.github.io/jq/)
* `kubectl` configured with existing cluster

## The cost of Bagan  <a name="cost"></a>
In case you are using Google Cloud as Kubernetes environment, you should consider the impact in terms of cost($$) of the experimentation. Android projects are expensive in terms of memory consumption 
and creating multiples combinations with not enough powerful machine will cause the failure of the experiments:


![alt text](resources/no_resources.png "No Resources")

Increasing the resources of the machine will help to succeed in the execution of the experiment, but it brings more consumption of resources a.k.a money. 
Remember that more permutations of the different experiments will generate more Pods. 
There is no restriction on the machine used in Bagan, for small/medium projects we recommend the n2 series(cost per hour):

![alt text](resources/cost.png "Cost")

Check more documentation about region, zones and availability in Gcloud here:
https://cloud.google.com/compute/docs/regions-zones/#available

![alt text](resources/alert.png "alert")

<b>In case you are using Google Cloud only to create the cluster for Bagan, we recommend to remove it after the experimentation and evaluation of the results have finished.</b>

## Examples  <a name="examples"></a>

### Example 1

Simple example of a private repository with three modules

| bagan_conf      |                                                               |
|-----------------|---------------------------------------------------------------|
| machine         |n2-standard-2                                                  |
| zone            |asia-southeast1-b                                              |
| repository      |git@github.com:cdsap/TestPrivateRepository.git                 |
| private         |true                                                           |
| experiments     |gradle properties :{ "org.gradle.jvmargs": ["-Xmx3g","-Xmx4g"]}|
| command         | ./gradlew clean assembleDebug                                 |
| iterations      |20                                                             |

Twp experiments of Type Gradle Properties will be generated: 
* -Xmx3g
* -Xmx4g

Result:

![alt text](resources/experiment1.png "experiment1")


### Example 2

Experimentation on Google project Plaid, with kapt properties

| bagan_conf      |                                                              |
|-----------------|--------------------------------------------------------------|
| machine         |n2-standard-8                                                 |
| zone            |asia-southeast1-b                                             |
| repository      |https://github.com/android/plaid.git                          |
| private         |false                                                         |
| experiments     |gradle properties:<br>{ "org.gradle.jvmargs": ["-Xmx2g","-Xmx4g"] }<br>{ "org.gradle.caching": ["true","false"] }<br>{ "kapt.incremental.apt": ["true","false"] }<br>{ "kapt.use.worker.api": ["true","false"] }<br>|
| command         | ./gradlew clean assembleDebug                                |
| iterations      |20                                                            |

 "properties": [
            {
               "name": "org.gradle.jvmargs",
               "options": ["-Xmx2g","-Xmx4g"]
            },
            {
               "name": "org.gradle.caching",
               "options": ["true","false"]
            },
            {
               "name": "kapt.incremental.apt",
               "options": ["true","false"]
            },
            {
               "name": "kapt.use.worker.api",
               "options": ["true","false"]
            }

16 experiments of Type Gradle Properties will be generated: 

|Experiments          |                                     | ||
|----------------------|-------------------------------------------|----|--------------------------------|
| org.gradle.jvmargs="-Xmx2g"<br>org.gradle.caching="true"<br>  kapt.incremental.apt="true" <br>kapt.use.worker.api="true" | org.gradle.jvmargs="-Xmx2g"<br> org.gradle.caching="true"<br>  kapt.incremental.apt="true" <br>kapt.use.worker.api="false" | org.gradle.jvmargs="-Xmx2g"<br> org.gradle.caching="true"<br>  kapt.incremental.apt="false" <br>kapt.use.worker.api="false" | org.gradle.jvmargs="-Xmx2g"<br> org.gradle.caching="true"<br>  kapt.incremental.apt="false" <br>kapt.use.worker.api="true" |
| org.gradle.jvmargs="-Xmx2g"<br>org.gradle.caching="false"<br>  kapt.incremental.apt="true" <br>kapt.use.worker.api="true" | org.gradle.jvmargs="-Xmx2g"<br> org.gradle.caching="false"<br>  kapt.incremental.apt="true" <br>kapt.use.worker.api="false" | org.gradle.jvmargs="-Xmx2g"<br> org.gradle.caching="false"<br>  kapt.incremental.apt="false" <br>kapt.use.worker.api="true" | org.gradle.jvmargs="-Xmx2g"<br> org.gradle.caching="false"<br>  kapt.incremental.apt="false" <br>kapt.use.worker.api="false" |
| org.gradle.jvmargs="-Xmx4g"<br>org.gradle.caching="true"<br>  kapt.incremental.apt="true" <br>kapt.use.worker.api="true" | org.gradle.jvmargs="-Xmx4g"<br> org.gradle.caching="true"<br>  kapt.incremental.apt="true" <br>kapt.use.worker.api="false" | org.gradle.jvmargs="-Xmx4g"<br> org.gradle.caching="true"<br>  kapt.incremental.apt="false" <br>kapt.use.worker.api="false" | org.gradle.jvmargs="-Xmx4g"<br> org.gradle.caching="true"<br>  kapt.incremental.apt="false" <br>kapt.use.worker.api="true" |
| org.gradle.jvmargs="-Xmx4g"<br>org.gradle.caching="false"<br>  kapt.incremental.apt="true" <br>kapt.use.worker.api="true" | org.gradle.jvmargs="-Xmx4g"<br> org.gradle.caching="false"<br>  kapt.incremental.apt="true" <br>kapt.use.worker.api="false" | org.gradle.jvmargs="-Xmx4g"<br> org.gradle.caching="false"<br>  kapt.incremental.apt="false" <br>kapt.use.worker.api="true" | org.gradle.jvmargs="-Xmx4g"<br> org.gradle.caching="false"<br>  kapt.incremental.apt="false" <br>kapt.use.worker.api="false" |


Result:

![alt text](resources/experiment2.png "experiment1")

We don't appreciate on Plaid project significant different using properties like `kapt.incremental.apt` and `kapt.use.worker.api`. However, you can notice the benefits of using caching on the Gradle Builds.

### Example 3

Experimentation on Android Showcase project, with Gradle properties and different Gradle Wrapper versions

| bagan_conf      |                                                              |
|-----------------|--------------------------------------------------------------|
| machine         |n2-standard-8                                           |
| zone            |asia-southeast1-b                                             |
| repository      |https://github.com/igorwojda/android-showcase             |
| private         |false                                                          |
| experiments     |gradle properties:<br>{ "org.gradle.jvmargs": ["-Xmx2g","-Xmx4g"] }<br>{ "org.gradle.caching": ["true","false"] }<br>{ "kapt.incremental.apt": ["true","false"] }<br>{ "kapt.use.worker.api": ["true","false"] }<br>Gradle Wrapper versions: ["5.6.1","5.5"]|
| command         | ./gradlew clean :app:bundleDebug                               |
| iterations      |20                                                            |

Result:

![alt text](resources/experiment3.png "experiment3")

Best times are using Gradle `5.6.1`. 

## Deploy  <a name="deploy"></a>
Bagan is open source and you can contribute or adapt it under your requirements. 
The folder structure of the project is:

| Folder               |
|----------------------|
| baganGenerator       | 
| deploy               |  
| docker               | 
| k8s                  | 
| scripts              |

### Bagan Generator
Bagan Generator is the Kotlin project with two main applications:
* Generation and installation of Experiments and Dashboards.
* Experiment controller handling the injection of Talaiot and experimentation on the pod.

We are using `kscript` to run the Kotlin classes. We need to transform the way we import the dependencies to be used in kscript. 
The task `convertFiles`(depending on `build` task) will convert the imports in a kscript format.

### Deploy 
Deployment folder used for:
 
* Build the BaganGenerator project.
* Copy the binaries required for each image to the folder
* Build, tag and push the docker image for bagan-init
* Build, tag  and push the docker image for bagan-pod 

### Docker 
It contains the Docker images for bagan-init and bagan-pod. It includes the binaries generated by the baganGenerator in the 
deployment step.
In case you want to provide your own images, you can set the values on the deployment file(`deploy/deployment.sh`):

```
VERSION="0.1.6"
REGISTRY=""
IMAGE_BAGAN_INIT="cdsap/bagan-init"
IMAGE_BAGAN_POD="cdsap/bagan-pod-injector"
```

### K8s
It contains the Grafana and InfluxDb charts.
Grafana contains the provisioned data source configuration for InfluxDb. Also includes the default dashboard that will be updated when experimentation will be executed.

### Scripts
It contains the bash scripts related to the verification and provisioning phase.  For each mode, there is available one command executor. 

## Tools/Libraries used in Bagan <a name="tools"></a>
* [Kubernetes](https://github.com/kubernetes/kubernetes) 
* [Google Cloud](https://cloud.google.com/)
* [Google Cloud SDK](https://cloud.google.com/sdk/)
* [Helm](https://github.com/helm/helm/)
* [jq](https://stedolan.github.io/jq/)
* [Talaiot](https://github.com/cdsap/Talaiot)
* [kscript](https://github.com/holgerbrandl/kscript) 
* [Gradle](https://gradle.org/)
* [Docker](https://www.docker.com/)

## The real Bagan <a name="bagan"></a>
Bagan is an ancient city and a UNESCO World Heritage Site located in the Mandalay Region of Myanmar.
If you have the opportunity, visit Bagan:
https://en.wikipedia.org/wiki/Bagan



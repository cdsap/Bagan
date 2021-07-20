package com.cdsap.bagan.generator

class Chart {
    fun transform(
        id: String,
        version: String
    ) = """
apiVersion: v1
appVersion: "$version"
description: Experiment resources like Pod and Configmap required in Bagan.
name: $id
version: $version"
    """.trimIndent()

}

class PodSecure {
    fun transform(bagan: Bagan) = """
apiVersion: v1
kind: Pod
metadata:
  name: {{ .Values.pod }}
  labels:
    app: bagan
    type: experiment
    session: {{ .Values.session }}
  annotations:
    seccomp.security.alpha.kubernetes.io/pod: 'docker/default'
spec:
  restartPolicy: Never
  initContainers:
  - name: git-sync
    image: k8s.gcr.io/git-sync-amd64:v2.0.6
    imagePullPolicy: Always
    volumeMounts:
    - name: service
      mountPath: /home/bagan/repo
    - name: git-secret
      mountPath: /etc/git-secret
    env:
    - name: GIT_SYNC_REPO
      value: {{ .Values.repository }}
    - name: GIT_SYNC_BRANCH
      value: {{ .Values.branch }}
    - name: GIT_SYNC_ROOT
      value: /home/bagan/repo
    - name: GIT_SYNC_DEST
      value: "agent"
    - name: GIT_SYNC_PERMISSIONS
      value: "0777"
    - name: GIT_SYNC_ONE_TIME
      value: "true"
    - name: GIT_SYNC_SSH
      value: "true"
    securityContext:
      runAsUser: 0
  containers:
  - name:  agent
    image: {{ .Values.image }}
    command: ["/bin/bash"]
    args: ["-c", "${ExecutorInPod.executor(bagan)}"]
    securityContext:
      runAsUser: 1714
      allowPrivilegeEscalation: true
      readOnlyRootFilesystem: false
    envFrom:
      - configMapRef:
          name: {{ .Values.configMaps }}
    volumeMounts:
      - name: service
        mountPath: /home/bagan/repo
  volumes:
    - name: service
      emptyDir: {}
    - name: git-secret
      secret:
        defaultMode: 256
        secretName: git-creds
""".trimIndent()

}

class Pod {
    fun transform(bagan: Bagan) = """
apiVersion: v1
kind: Pod
metadata:
  name: {{ .Values.pod }}
  labels:
    app: bagan
    type: experiment
    session: {{ .Values.session }}
  annotations:
    seccomp.security.alpha.kubernetes.io/pod: 'docker/default'
spec:
  restartPolicy: Never
  initContainers:
  - name: git-sync
    image: k8s.gcr.io/git-sync-amd64:v2.0.6
    imagePullPolicy: Always
    volumeMounts:
    - name: service
      mountPath: /home/bagan/repo
    env:
    - name: GIT_SYNC_REPO
      value: {{ .Values.repository }}
    - name: GIT_SYNC_BRANCH
      value: {{ .Values.branch }}
    - name: GIT_SYNC_ROOT
      value: /home/bagan/repo
    - name: GIT_SYNC_DEST
      value: "agent"
    - name: GIT_SYNC_PERMISSIONS
      value: "0777"
    - name: GIT_SYNC_ONE_TIME
      value: "true"
    - name: GIT_SYNC_SSH
      value: "false"
    securityContext:
      runAsUser: 0
  containers:
  - name:  agent
    image: {{ .Values.image }}
    command: ["/bin/bash"]
    args: ["-c", "${ExecutorInPod.executor(bagan)}"]
    securityContext:
      runAsUser: 1714
      allowPrivilegeEscalation: true
      readOnlyRootFilesystem: false
    envFrom:
      - configMapRef:
          name: {{ .Values.configMaps }}
    volumeMounts:
      - name: service
        mountPath: /home/bagan/repo
  volumes:
    - name: service
      emptyDir: {}
""".trimIndent()
}

class Values {
    fun transform(
        repository: String,
        name: String,
        command: String,
        iterations: Int,
        image: String,
        session: String,
        branch: String
    ) = """
repository: $repository
branch: $branch
configMaps: configmap$name
pod: $name
session: $session
name: $name
image: $image
command: $command
iterations: $iterations
""".trimIndent()
}

class ConfigMap {
    fun transform(
        experiments: String = ""

    ) = """
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
  $experiments
""".trimIndent()
}

object ExecutorInPod {
    fun executor(bagan: Bagan) = """
source ../.sdkman/bin/sdkman-init.sh;
${moveIfIsRequiredScenarioFile(bagan)}
mv *.kt ../repo/agent;
cd ../repo/agent;
kscript ExperimentController.kt;
${providerMode(bagan)}
""".trimIndent()
}

fun providerMode(bagan: Bagan): String {
    var scenarioString = ""
    if (bagan.scenarioFile != null && bagan.scenarioName != null) {
        scenarioString = "--scenario-file ${bagan.scenarioFile} ${bagan.scenarioName}"
    } else {
        scenarioString = bagan.gradleCommand
    }
    return "gradle-profiler --benchmark --project-dir . --warmups ${bagan.warmups} --iterations ${bagan.iterations} $scenarioString"
}

fun moveIfIsRequiredScenarioFile(bagan: Bagan): String {
    var scenarioFile = ""
    if (bagan.scenarioFile != null && bagan.scenarioName != null) {
        scenarioFile = "mv ${bagan.scenarioFile} ../repo/agent"
    }
    return scenarioFile
}

object ConfigMapExperiments {
    fun branch(branch: String) = """branch: $branch"""
    fun gradleWrapperVersion(version: String) = """gradleWrapperVersion: '$version'"""
    fun properties(properties: String) =
        """properties: |
               $properties""".trimIndent()

}
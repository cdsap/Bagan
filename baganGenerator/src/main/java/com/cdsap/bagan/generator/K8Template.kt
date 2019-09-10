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
    fun transform() = """
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
      mountPath: /repo
    - name: git-secret
      mountPath: /etc/git-secret
    env:
    - name: GIT_SYNC_REPO
      value: {{ .Values.repository }}
    - name: GIT_SYNC_BRANCH
      value: {{ .Values.branch }}
    - name: GIT_SYNC_ROOT
      value: /repo
    - name: GIT_SYNC_DEST
      value: "workspace"
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
    args: ["-c", "${ExecutorInPod.executor()}"]
    securityContext:
      runAsUser: 0
      allowPrivilegeEscalation: true
      readOnlyRootFilesystem: false
    envFrom:
      - configMapRef:
          name: {{ .Values.configMaps }}
    volumeMounts:
      - name: service
        mountPath: /repo
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
    fun transform() = """
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
      mountPath: /repo
    env:
    - name: GIT_SYNC_REPO
      value: {{ .Values.repository }}
    - name: GIT_SYNC_BRANCH
      value: {{ .Values.branch }}
    - name: GIT_SYNC_ROOT
      value: /repo
    - name: GIT_SYNC_DEST
      value: "workspace"
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
    args: ["-c", "${ExecutorInPod.executor()}"]
    securityContext:
      runAsUser: 0
      allowPrivilegeEscalation: true
      readOnlyRootFilesystem: false
    envFrom:
      - configMapRef:
          name: {{ .Values.configMaps }}
    volumeMounts:
      - name: service
        mountPath: /repo
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
        experiments: String = "",
        talaiotProperties: String = ""
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
  $talaiotProperties
""".trimIndent()
}

object ExecutorInPod {
    fun executor() = """
mv *.kt /repo/workspace;
cd /repo/workspace;
source /root/.bashrc;
source /usr/share/sdkman/bin/sdkman-init.sh;
source /root/.bashrc;
kscript ExperimentController.kt;
for i in `seq 1 {{ .Values.iterations }}`; do {{ .Values.command }}; done;
""".trimIndent()
}


object ConfigMapExperiments {
    fun branch(branch: String) = """branch: $branch"""
    fun gradleWrapperVersion(version: String) = """gradleWrapperVersion: '$version'"""
    fun properties(properties: String) =
        """properties: |
               $properties""".trimIndent()

}
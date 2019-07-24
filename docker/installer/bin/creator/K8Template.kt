package com.cdsap.bagan.experiments

class Chart {
    fun transform(
        id: String,
        version: String
    ) = """
apiVersion: v1
appVersion: "1.0"
description: Chart, used for Bagan resource
name: $id
version: $version"
    """.trimIndent()

}

class Pod {
    fun transform(
        nameExperiment: String,
        experiment: String
    ) = """
apiVersion: v1
kind: Pod
metadata:
  name: $nameExperiment
  labels:
    app: experiment
    type: experiment
    experimentid: $experiment
  annotations:
    seccomp.security.alpha.kubernetes.io/pod: 'docker/default'
spec:
  initContainers:
    - name: git-clone
      image: alpine/git
      args:
        - clone
        - --single-branch
        - --
        - {{ .Values.repository }}
        - /repo
      securityContext:
        runAsUser: 1
        allowPrivilegeEscalation: false
        readOnlyRootFilesystem: true
      volumeMounts:
        - name: git-repo
          mountPath: /repo
  containers:
    - name:  agent
      image: {{ .Values.image }}
      args: ['sleep', '100000']
      envFrom:
        - configMapRef:
            name: {{ .Values.configMaps }}
      volumeMounts:
        - name: git-repo
          mountPath: /repo
      lifecycle:
            postStart:
              exec:
                command:
                - bash
                - -c
                - |
                  mv *.kt /repo
                  cd /repo
                  source /usr/share/sdkman/bin/sdkman-init.sh
                  source /root/.bashrc
                  kscript TalaiotInjector.kt
                  kscript RewriteProperties.kt
                  for i in `seq 1 {{ .Values.iterations }}`; do {{ .Values.command }}; done
  volumes:
    - name: git-repo
      emptyDir: {}
    """.trimIndent()
}

class Values {
    fun transform(
        repository: String,
        configMap: String,
        name: String,
        command: String,
        iterations: Int
    ) = """
repository: $repository
configMaps : configmap$name
name : $name
image: cdsap/bagan-pod-injector
command: $command
iterations: $iterations
""".trimIndent()
}

class ConfigMap {
    fun transform(
        nameExperiment: String,
        properties: String
    ) = """
apiVersion: v1
kind: ConfigMap
metadata:
  name: configmap$nameExperiment
  labels:
    type: experiment
    experiment_id: $nameExperiment
data:
  id: $nameExperiment
  experiments: |
               $properties""".trimIndent()
}
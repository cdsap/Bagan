package com.cdsap.bagan.generator

import io.kotlintest.specs.BehaviorSpec
//
//class PodTest : BehaviorSpec({
//    given("Pod Secure") {
//        `when`("Parameters are defined with secret") {
//            val values = PodSecure().transform()
//            val x = """
//apiVersion: v1
//kind: Pod
//metadata:
//  name: {{ .Values.pod }}
//  labels:
//    app: bagan
//    type: experiment
//    session: {{ .Values.session }}
//  annotations:
//    seccomp.security.alpha.kubernetes.io/pod: 'docker/default'
//spec:
//  restartPolicy: Never
//  initContainers:
//  - name: git-sync
//    image: k8s.gcr.io/git-sync-amd64:v2.0.6
//    imagePullPolicy: Always
//    volumeMounts:
//    - name: service
//      mountPath: /repo
//    - name: git-secret
//      mountPath: /etc/git-secret
//    env:
//    - name: GIT_SYNC_REPO
//      value: {{ .Values.repository }}
//    - name: GIT_SYNC_BRANCH
//      value: {{ .Values.branch }}
//    - name: GIT_SYNC_ROOT
//      value: /repo
//    - name: GIT_SYNC_DEST
//      value: "workspace"
//    - name: GIT_SYNC_PERMISSIONS
//      value: "0777"
//    - name: GIT_SYNC_ONE_TIME
//      value: "true"
//    - name: GIT_SYNC_SSH
//      value: "true"
//    securityContext:
//      runAsUser: 0
//  containers:
//  - name:  agent
//    image: {{ .Values.image }}
//    command: ["/bin/bash"]
//    args: ["-c", "mv *.kt /repo/workspace;
//cd /repo/workspace;
//source /root/.bashrc;
//source /usr/share/sdkman/bin/sdkman-init.sh;
//source /root/.bashrc;
//kscript ExperimentController.kt;
//for i in `seq 1 {{ .Values.iterations }}`; do {{ .Values.command }}; done;"]
//    securityContext:
//      runAsUser: 0
//      allowPrivilegeEscalation: true
//      readOnlyRootFilesystem: false
//    envFrom:
//      - configMapRef:
//          name: {{ .Values.configMaps }}
//    volumeMounts:
//      - name: service
//        mountPath: /repo
//  volumes:
//    - name: service
//      emptyDir: {}
//    - name: git-secret
//      secret:
//        defaultMode: 256
//        secretName: git-creds
//""".trimIndent()
//            then("pod template have been placed with secret") {
//
//                assert(
//                    values == x
//                )
//            }
//
//        }
//
//        `when`("Parameters are defined without secret") {
//            val values = Pod().transform()
//            then("pod template have been placed without secret") {
//                assert(
//                    values.trimIndent() == """
//apiVersion: v1
//kind: Pod
//metadata:
//  name: {{ .Values.pod }}
//  labels:
//    app: bagan
//    type: experiment
//    session: {{ .Values.session }}
//  annotations:
//    seccomp.security.alpha.kubernetes.io/pod: 'docker/default'
//spec:
//  restartPolicy: Never
//  initContainers:
//  - name: git-sync
//    image: k8s.gcr.io/git-sync-amd64:v2.0.6
//    imagePullPolicy: Always
//    volumeMounts:
//    - name: service
//      mountPath: /repo
//    env:
//    - name: GIT_SYNC_REPO
//      value: {{ .Values.repository }}
//    - name: GIT_SYNC_BRANCH
//      value: {{ .Values.branch }}
//    - name: GIT_SYNC_ROOT
//      value: /repo
//    - name: GIT_SYNC_DEST
//      value: "workspace"
//    - name: GIT_SYNC_PERMISSIONS
//      value: "0777"
//    - name: GIT_SYNC_ONE_TIME
//      value: "true"
//    - name: GIT_SYNC_SSH
//      value: "false"
//    securityContext:
//      runAsUser: 0
//  containers:
//  - name:  agent
//    image: {{ .Values.image }}
//    command: ["/bin/bash"]
//    args: ["-c", "mv *.kt /repo/workspace;
//cd /repo/workspace;
//source /root/.bashrc;
//source /usr/share/sdkman/bin/sdkman-init.sh;
//source /root/.bashrc;
//kscript ExperimentController.kt;
//for i in `seq 1 {{ .Values.iterations }}`; do {{ .Values.command }}; done;"]
//    securityContext:
//      runAsUser: 0
//      allowPrivilegeEscalation: true
//      readOnlyRootFilesystem: false
//    envFrom:
//      - configMapRef:
//          name: {{ .Values.configMaps }}
//    volumeMounts:
//      - name: service
//        mountPath: /repo
//  volumes:
//    - name: service
//      emptyDir: {}
//""".trimIndent()
//                )
//            }
//
//        }
//        `when`("Execution is Experiment Composed") {
//            val values = PodSecureComposed().transform()
//            then("pod template have been placed with composed") {
//                assert(
//                    values.trimIndent() == """
//apiVersion: v1
//kind: Pod
//metadata:
//  name: {{ .Values.pod }}
//  labels:
//    app: bagan
//    type: experiment
//    session: {{ .Values.session }}
//  annotations:
//    seccomp.security.alpha.kubernetes.io/pod: 'docker/default'
//spec:
//  restartPolicy: Never
//  initContainers:
//  - name: git-sync
//    image: k8s.gcr.io/git-sync-amd64:v2.0.6
//    imagePullPolicy: Always
//    volumeMounts:
//    - name: service
//      mountPath: /repo
//    - name: git-secret
//      mountPath: /etc/git-secret
//    env:
//    - name: GIT_SYNC_REPO
//      value: {{ .Values.repository }}
//    - name: GIT_SYNC_BRANCH
//      value: {{ .Values.branch }}
//    - name: GIT_SYNC_ROOT
//      value: /repo
//    - name: GIT_SYNC_DEST
//      value: "workspace"
//    - name: GIT_SYNC_PERMISSIONS
//      value: "0777"
//    - name: GIT_SYNC_ONE_TIME
//      value: "true"
//    - name: GIT_SYNC_SSH
//      value: "true"
//    securityContext:
//      runAsUser: 0
//  containers:
//  - name:  agent
//    image: {{ .Values.image }}
//    command: ["/bin/bash"]
//    args: ["-c", "${ExecutorInPodComposed.executor()}"]
//    securityContext:
//      runAsUser: 0
//      allowPrivilegeEscalation: true
//      readOnlyRootFilesystem: false
//    envFrom:
//      - configMapRef:
//          name: {{ .Values.configMaps }}
//    volumeMounts:
//      - name: service
//        mountPath: /repo
//  volumes:
//    - name: service
//      emptyDir: {}
//    - name: git-secret
//      secret:
//        defaultMode: 256
//        secretName: git-creds
//""".trimIndent()
//                )
//            }
//
//        }
//
//    }
//})
//

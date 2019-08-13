package com.cdsap.bagan.experiments

import io.kotlintest.specs.BehaviorSpec

class PodTest : BehaviorSpec({
    given("Pod file") {
        val nameExperiment = "experiment0"
        val experiment = "experiment"
        `when`("Parameters are defined") {
            val values = Pod().transform(
                nameExperiment = nameExperiment,
                experiment = experiment
            )
            then("pod template have been placed") {
                assert(
                    values == """
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
      value: master
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
    args: ["-c", "mv *.kt /repo/workspace; cd /repo/workspace; source /root/.bashrc; source /usr/share/sdkman/bin/sdkman-init.sh; source /root/.bashrc;  kscript TalaiotInjector.kt;  kscript RewriteProperties.kt; for i in `seq 1 {{ .Values.iterations }}`; do {{ .Values.command }}; done; "]
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
                )
            }

        }
    }
})


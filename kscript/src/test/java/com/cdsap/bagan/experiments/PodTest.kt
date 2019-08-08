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
      command: ["/bin/sh"]
      args: ["-c", "mv *.kt /repo; cd /repo;  cat /root/.bashrc;  source /root/.bashrc;  ls; source /usr/share/sdkman/bin/sdkman-init.sh; source /root/.bashrc;  kscript TalaiotInjector.kt;  kscript RewriteProperties.kt; for i in `seq 1 {{ .Values.iterations }}`; do {{ .Values.command }}; done; "]
      securityContext:
        runAsUser: 0
        allowPrivilegeEscalation: true
        readOnlyRootFilesystem: false
      envFrom:
        - configMapRef:
            name: {{ .Values.configMaps }}
      volumeMounts:
        - name: git-repo
          mountPath: /repo
  volumes:
    - name: git-repo
      emptyDir: {}
    """.trimIndent()
                )
            }

        }
    }
})


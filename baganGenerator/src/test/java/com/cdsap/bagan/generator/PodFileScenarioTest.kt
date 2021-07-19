package com.cdsap.bagan.generator

import io.kotest.core.spec.style.BehaviorSpec
import java.io.File


class PodFileScenarioTest : BehaviorSpec({
    given("Pod Secure with File Scenario") {
        val file = File("scenario")
        val bagan = Bagan(
            repository = "http ://git.com",
            gradleCommand = "assemble",
            clusterName = "myCluster",
            zone = "myZone",
            project_id = "",
            experiments = getSimpleExperiment(),
            iterations = 10,
            private = true,
            scenarioFile = file,
            scenarioName = "incrementalChange"
        )
        `when`("Parameters are defined with secret") {


            val values = PodSecure().transform(bagan)
            val x = """
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
    args: ["-c", "source ../.sdkman/bin/sdkman-init.sh;
mv scenario ../repo/agent
mv *.kt ../repo/agent;
cd ../repo/agent;
kscript ExperimentController.kt;
gradle-profiler --benchmark --project-dir . --warmups 2 --iterations 10 --scenario-file scenario incrementalChange"]
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
            then("pod template have been placed with secret") {
                assert(
                    values == x
                )
            }
        }

        `when`("Parameters are defined without secret") {
            val values = Pod().transform(bagan)
            println(values)
            then("pod template have been placed without secret") {
                assert(
                    values.trimIndent() == """
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
    args: ["-c", "source ../.sdkman/bin/sdkman-init.sh;
mv scenario ../repo/agent
mv *.kt ../repo/agent;
cd ../repo/agent;
kscript ExperimentController.kt;
gradle-profiler --benchmark --project-dir . --warmups 2 --iterations 10 --scenario-file scenario incrementalChange"]
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
                )
            }

        }
        file.delete()
    }

})


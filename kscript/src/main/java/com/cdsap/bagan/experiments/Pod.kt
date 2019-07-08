package com.cdsap.bagan.experiments

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
                  cd /usr/share/sdkman/bin 
                  source sdkman-generate.sh
                  cd /repo
                  kscript TalaiotInjector.kt 
                  kscript RewriteProperties.kt 
                  pwd >  /usr/share/message
                  for i in `seq 1 {{ .Values.iterations }}`; do {{ .Values.command }}; done
  volumes:
    - name: git-repo
      emptyDir: {}\n"
    """.trimIndent()
}
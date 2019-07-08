package com.cdsap.bagan.experiments

class ConfigMap {
    fun transform(
        nameConfigMap: String,
        nameExperiment: String,
        properties: String
    ) = """
apiVersion: v1
kind: ConfigMap
metadata:
  name: $nameConfigMap
  labels:
    type: experiment
    experiment_id: $nameExperiment
data:
  id: $nameExperiment
  experiments: |
               $properties

    """.trimIndent()
}
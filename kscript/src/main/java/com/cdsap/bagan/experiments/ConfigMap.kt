package com.cdsap.bagan.experiments

object ConfigMap {
    val template = """
apiVersion: v1
kind: ConfigMap
metadata:
  name: $s1
  labels:
    type: experiment
    experiment_id: $nameExperiment
data:
  id: $nameExperiment
  experiments: |
               $propertyName

    """.trimIndent()
}
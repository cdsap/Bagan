package com.cdsap.bagan.experiments

import io.kotlintest.specs.BehaviorSpec

class ConfigMapTest : BehaviorSpec({
    given("ConfigMap file") {
        val nameConfigMap = "experiment0configmap"
        val nameExperiment = "exoeriment0"
        val properties = "org.jvm.arg=1v" +
                "    org.gradle.caching=true"
        `when`("Parameters are defined") {
            val values = ConfigMap().transform(
                nameConfigMap = nameConfigMap,
                nameExperiment = nameExperiment,
                properties = properties
            )
            then("configmap template have been placed") {
                assert(
                    values == """
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
                )
            }

        }
    }
})


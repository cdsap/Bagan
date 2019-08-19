package com.cdsap.bagan.experiments

import io.kotlintest.specs.BehaviorSpec

class ConfigMapTest : BehaviorSpec({
    given("ConfigMap file") {
        val properties = "org.jvm.arg=1v" +
                "    org.gradle.caching=true"
        `when`("Parameters are defined") {
            val values = ConfigMap().transform(
                properties = properties
            )
            then("configmap template have been placed") {
                assert(
                    values == """
apiVersion: v1
kind: ConfigMap
metadata:
  name: {{ .Values.configMaps }}
  labels:
    app: bagan
    type: experiment
    session: {{ .Values.session }}
data:
  id: {{ .Values.configMaps }}
  experiments: |
               $properties
""".trimIndent()
                )
            }

        }
    }
})


package com.cdsap.bagan.generator

import io.kotest.core.spec.style.BehaviorSpec


class ConfigMapTest : BehaviorSpec({
    given("ConfigMap file") {
        `when`("Parameter branch is defined") {
            val values = ConfigMap().transform(
                ConfigMapExperiments.branch("develop")
            )
            then("configmap template have been placed with Branch conf") {
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
  id: {{ .Values.name }}
  branch: develop
""".trimIndent()
                )
            }

        }
        `when`("Parameter Gradle Wrapper Version is defined") {
            val values = ConfigMap().transform(
                ConfigMapExperiments.gradleWrapperVersion("4.3")
            )
            then("configmap template have been placed with Gradle Wrapper Version conf") {
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
  id: {{ .Values.name }}
  gradleWrapperVersion: '4.3'
""".trimIndent()
                )
            }

        }
        `when`("Parameter Properties is defined") {
            val values = ConfigMap().transform(
                ConfigMapExperiments.properties("property1=a")
            )
            then("configmap template have been placed with properties conf") {
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
  id: {{ .Values.name }}
  properties: |
               property1=a
""".trimIndent()
                )
            }

        }
        `when`("All type exepriments are defined") {
            val values = ConfigMap().transform(
                ConfigMapExperiments.properties("property1=a") + "\n" +
                        "  " + ConfigMapExperiments.branch("develop") + "\n" +
                        "  " + ConfigMapExperiments.gradleWrapperVersion("4.5")


            )
            then("configmap template have been placed with properties conf") {
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
  id: {{ .Values.name }}
  properties: |
               property1=a
  branch: develop
  gradleWrapperVersion: '4.5'""".trimIndent()

                )
            }

        }
    }
})



package com.cdsap.bagan.generator

import io.kotlintest.specs.BehaviorSpec

class ConfigMapTest : BehaviorSpec({
    given("ConfigMap file") {
        `when`("Parameter branch is defined") {
            val values = ConfigMap().transform(
                false,
                ConfigMapExperiments.branch("develop")
            )
            println(values)
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
  isComposed: 'false'
  branch: develop
""".trimIndent()
                )
            }

        }
        `when`("Parameter Gradle Wrapper Version is defined") {
            val values = ConfigMap().transform(
                false,
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
  isComposed: 'false'
  gradleWrapperVersion: '4.3'
""".trimIndent()
                )
            }

        }
        `when`("Parameter Properties is defined") {
            val values = ConfigMap().transform(
                false,
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
  isComposed: 'false'
  properties: |
               property1=a
""".trimIndent()
                )
            }

        }
        `when`("All type exepriments are defined") {
            val values = ConfigMap().transform(
                false,
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
  isComposed: 'false'
  properties: |
               property1=a
  branch: develop
  gradleWrapperVersion: '4.5'""".trimIndent()
                )
            }
        }
    }
})



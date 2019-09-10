package com.cdsap.bagan.generator

import io.kotlintest.matchers.string.shouldContain
import io.kotlintest.specs.BehaviorSpec

class ConfigMapTest : BehaviorSpec({
    given("ConfigMap file") {
        `when`("Parameter branch is defined") {
            val values = ConfigMap().transform(
                ConfigMapExperiments.branch("develop")
            )
            then("configmap template have been placed with Branch conf") {
                values shouldContain ("""
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
                values shouldContain ("""
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
                values shouldContain ("""
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
        `when`("All type experiments are defined") {
            val values = ConfigMap().transform(
                ConfigMapExperiments.properties("property1=a") + "\n" +
                        "  " + ConfigMapExperiments.branch("develop") + "\n" +
                        "  " + ConfigMapExperiments.gradleWrapperVersion("4.5")


            )
            then("configmap template have been placed with properties conf") {
                values shouldContain ("""
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
  gradleWrapperVersion: '4.5'
  """.trimIndent()
                        )
            }

        }
        `when`("Talaiot task and build properties are defined") {
            var talaiotProperties = ""
            talaiotProperties += "  talaiot.publishTaskMetrcis: true\n"
            talaiotProperties += "  talaiot.publishBuildMetrcis: true"

            val values = ConfigMap().transform(
                ConfigMapExperiments.properties("property1=a"),
                talaiotProperties
            )
            then("Talaiot properties are present in the ConfigMap file") {
                values shouldContain "  talaiot.publishTaskMetrcis: true"
                values shouldContain "  talaiot.publishBuildMetrcis: true"
                values shouldContain "               property1=a"
            }
        }

    }
})



package com.cdsap.bagan.experiments

import java.io.File
import java.io.FileNotFoundException

enum class MODE {
    GROOVY,
    KTS
}

class TalaiotInjector(
    private val path: String,
    private val logger: LoggerPod
) {

    private val TAG = "TalaiotInjector"
    fun init() {
        logger.log(TAG, "Begin process")
        val mode = checkMode()
        logger.log(TAG, "Mode $mode")
        appendTalaiot(mode)
        createFileTalaiot()
        logger.log(TAG, "End process")
    }

    private fun checkMode(): MODE {
        return when {
            gradleFileExists("$path/build.gradle.kts") -> MODE.KTS
            gradleFileExists("$path/build.gradle") -> MODE.GROOVY
            else -> {
                logger.log(TAG, "Main Build Gradle not found")
                throw FileNotFoundException("Main Build Gradle not found, looking for build.gradle / build.gradlew.kts")
            }
        }
    }

    private fun gradleFileExists(path: String) = File(path).exists()

    private fun appendTalaiot(mode: MODE) {
        when (mode) {
            MODE.GROOVY -> {
                File("$path/build.gradle").appendText("\napply from : \"talaiot.gradle.kts\"")
            }
            MODE.KTS -> {
                File("$path/build.gradle.kts").appendText("\napply(from = \"talaiot.gradle.kts\")")
            }
        }
    }

    private fun createFileTalaiot() {
        val id = System.getenv("id")
        val file = File("$path/talaiot.gradle.kts")
        val content = """
buildscript {
    repositories {
        mavenCentral()
        google()
        mavenLocal()
        jcenter()
    }
    dependencies {
        classpath("com.cdsap:talaiot:1.0.4")
    }
}

apply<com.cdsap.talaiot.TalaiotPlugin>()

configure<com.cdsap.talaiot.TalaiotExtension>() {
    logger = com.cdsap.talaiot.logger.LogTracker.Mode.INFO
    metrics {
        customBuildMetrics("experiment" to  "$id")
        customTaskMetrics("experiment" to  "$id")
    }
    publishers {

        influxDbPublisher {
            dbName = "tracking"
            url = "http://bagan-influxdb.default:8086"
            taskMetricName = "tasks"
            buildMetricName = "build"
        }
    }
}
""".trimIndent()
        file.writeText(content)
    }
}

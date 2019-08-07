package com.cdsap.bagan.injector

import java.io.File

enum class MODE {
    GROOVY,
    KTS
}

fun main() {
    TalaiotInjector().init()
}

class TalaiotInjector {

    fun init() {
        println("[TalaiotInjector]: Begin process")
        val mode = checkMode()
        println("[TalaiotInjector]: Mode $mode")
        appendTalaiot(mode)
        createFileTalaiot()
        println("[TalaiotInjector]: End process")
    }

    private fun checkMode(): MODE {
        return when {
            gradleFileExists("build.gradle.kts") -> MODE.KTS
            gradleFileExists("build.gradle") -> MODE.GROOVY
            else -> throw java.lang.Exception("Main Build Gradle not found, looking for build.gradle / build.gradlew.kts")
        }
    }

    private fun gradleFileExists(path: String) = File(path).exists()

    private fun appendTalaiot(mode: MODE) {
        when (mode) {
            MODE.GROOVY -> {
                File("build.gradle").appendText("\napply from : \"talaiot.gradle.kts\"")
            }
            MODE.KTS -> {
                File("build.gradle.kts").appendText("\napply(from = \"talaiot.gradle.kts\")")
            }
        }
    }

    private fun createFileTalaiot() {
        val id = System.getenv("id")

        val file = File("talaiot.gradle.kts")
        file.writeText(
            "" +
                    "buildscript {\n" +
                    "    repositories {\n" +
                    "        mavenCentral()\n" +
                    "        google()\n" +
                    "        mavenLocal()\n" +
                    "        jcenter()\n" +
                    "\n" +
                    "    }\n" +
                    "    dependencies {\n" +
                    "        classpath(\"com.cdsap:talaiot:0.4.0\")\n" +
                    "    }\n" +
                    "}\n" +
                    "\n" +
                    "apply<com.cdsap.talaiot.TalaiotPlugin>()\n" +
                    "configure<com.cdsap.talaiot.TalaiotExtension>() {\n" +
                    "    logger = com.cdsap.talaiot.logger.LogTracker.Mode.INFO\n" +
                    "    metrics { customMetrics(\"experiment\" to  \"$id\") } \n" +
                    "    publishers {\n" +
                    "\n" +
                    "        influxDbPublisher {\n" +
                    "            dbName = \"tracking\"\n" +
                    "            url = \"http://bagan-influxdb.default:8086\"\n" +
                    "            urlMetric = \"tracking\"\n" +
                    "        }\n" +
                    "    }\n" +
                    "}"
        )
    }
}

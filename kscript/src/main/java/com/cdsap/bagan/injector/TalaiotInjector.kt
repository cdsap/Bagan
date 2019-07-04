package com.cdsap.bagan.injector

import java.io.File
import java.io.StringReader

enum class MODE {
    GROOVY,
    KTS
}

fun main() {

    TalaiotInjector().init()

}

class TalaiotInjector {
    fun init() {
        val mode = checkMode()
        appendTalaiot(mode)
        createFileTalaiot(mode)
    }

    fun checkMode(): MODE {
        if (gradleFileExists("build.gradle.kts")) {
            return MODE.KTS
        } else if (gradleFileExists("build.gradle")) {
            return MODE.GROOVY
        } else {
            throw java.lang.Exception("Main Build Gradle not found, looking for build.gradle / build.gradlew.kts")
        }
    }

    fun gradleFileExists(path: String) = File(path).exists()

    fun appendTalaiot(mode: MODE) {
        when (mode) {
            MODE.GROOVY -> {
                File("build.gradle").appendText("\napply from : \"talaiot.gradle.kts\"")
            }
            MODE.KTS -> {
                File("build.gradle.kts").appendText("\napply(from = \"talaiot.gradle.kts\")")
            }
        }
    }

    fun createFileTalaiot(mode: MODE) {
        val id = System.getenv("id")
        println(id.toString())
        println(id)

        when (mode) {
            MODE.GROOVY -> {
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
            MODE.KTS -> {
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
    }


}
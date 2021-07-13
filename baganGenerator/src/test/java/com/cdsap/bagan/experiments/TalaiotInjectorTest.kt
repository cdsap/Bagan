package com.cdsap.bagan.experiments

import com.cdsap.bagan.utils.TestFolder
import com.cdsap.bagan.utils.TestPodLogger
import io.kotlintest.extensions.system.withEnvironment

import io.kotlintest.matchers.haveSubstring
import io.kotlintest.matchers.startWith
import io.kotlintest.should
import io.kotlintest.shouldThrow
import io.kotlintest.specs.BehaviorSpec
import java.io.File
import java.io.FileNotFoundException


class TalaiotInjectorTest : BehaviorSpec({
    TestFolder.recursiveDelete(File("tmp"))
    given("TalaiotInjector instance") {
        `when`("build gradle doesn't exiss") {
            val testLogger = TestPodLogger()
            File("tmp").mkdir()
            val talaiotInjector = TalaiotInjector("tmp", testLogger)
            val exception = shouldThrow<FileNotFoundException> { talaiotInjector.init() }

            then("error message") {
                exception.message should startWith("Main Build Gradle not found")
            }
            TestFolder.recursiveDelete(File("tmp"))

        }
        `when`("build.gradle.kts exists") {
            val testLogger = TestPodLogger()
            File("tmp").mkdir()
            File("tmp/build.gradle.kts").createNewFile()
            val talaiotInjector = TalaiotInjector("tmp", testLogger)
            talaiotInjector.init()
            then("the import of talaiot.gradle.kts matches with the kts format") {
                assert(testLogger.containsLog("Mode KTS"))
                val file = File("tmp/build.gradle.kts")
                file.readText() should haveSubstring("apply(from = \"talaiot.gradle.kts\"")

            }
            TestFolder.recursiveDelete(File("tmp"))

        }

        `when`("build.gradle exists") {
            val testLogger = TestPodLogger()
            File("tmp").mkdir()
            File("tmp/build.gradle").createNewFile()
            val talaiotInjector = TalaiotInjector("tmp", testLogger)
            talaiotInjector.init()
            then("the import of talaiot.gradle.kts matches with the groovy format") {
                assert(testLogger.containsLog("Mode GROOVY"))
                val file = File("tmp/build.gradle")
                file.readText() should haveSubstring("apply from : \"talaiot.gradle.kts\"")

            }
            TestFolder.recursiveDelete(File("tmp"))

        }
        `when`("talaiot.gradle.kts has been created") {
            val testLogger = TestPodLogger()
            File("tmp").mkdir()
            File("tmp/build.gradle").createNewFile()
            val talaiotInjector = TalaiotInjector("tmp", testLogger)
            talaiotInjector.init()
            then("the import of talaiot.gradle.kts matches with the groovy format") {
                val file = File("tmp/talaiot.gradle.kts")
                val content = """
buildscript {
  repositories {
    maven {
       url = uri("https://plugins.gradle.org/m2/")
      }
    }
    dependencies {
        classpath("com.cdsap:talaiot:1.4.0")
    }
}

apply<com.cdsap.talaiot.plugin.TalaiotPlugin>()

configure<com.cdsap.talaiot.plugin.TalaiotPluginExtension>() {
    logger = com.cdsap.talaiot.logger.LogTracker.Mode.INFO
    metrics {
        customBuildMetrics("experiment" to  "null")
        customTaskMetrics("experiment" to  "null")
    }
    publishers {

        influxDbPublisher {
            dbName = "tracking"
            publishBuildMetrics = true
            publishTaskMetrics = true
            url = "http://bagan-influxdb.default:8086"
            taskMetricName = "tasks"
            buildMetricName = "build"
        }
    }
}
""".trimIndent()
                file.readText() should haveSubstring(content)
            }
            TestFolder.recursiveDelete(File("tmp"))
        }
        `when`("Publish Task metrics is false but Publish Build metrics is true") {
            withEnvironment("talaiot.publishTaskMetrics" to "false") {
                val testLogger = TestPodLogger()
                File("tmp").mkdir()
                File("tmp/build.gradle").createNewFile()
                val talaiotInjector = TalaiotInjector("tmp", testLogger)
                talaiotInjector.init()

            }

            then("exception is catched and log message is registered") {
                val file = File("tmp/talaiot.gradle.kts")
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
        customBuildMetrics("experiment" to  "null")
        customTaskMetrics("experiment" to  "null")
    }
    publishers {

        influxDbPublisher {
            dbName = "tracking"
            publishBuildMetrics = true
            publishTaskMetrics = false
            url = "http://bagan-influxdb.default:8086"
            taskMetricName = "tasks"
            buildMetricName = "build"
        }
    }
}""".trimIndent()
            }
            TestFolder.recursiveDelete(File("tmp"))
        }


    }
})

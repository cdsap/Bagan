package com.cdsap.bagan.experiments

import com.cdsap.bagan.utils.TestFolder
import com.cdsap.bagan.utils.TestPodLogger
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.system.withEnvironment

import java.io.File


class ExperimentControllerTest : BehaviorSpec({
    given("ExperimentController instance") {
        `when`("TalaiotInjector is called") {
            File("tmp").mkdir()
            File("tmp/build.gradle").createNewFile()
            val testLogger = TestPodLogger()
            val experimentController = ExperimentController("tmp", testLogger)
            experimentController.init()
            then("log message is registered") {
                assert(testLogger.containsLog("Begin process"))
            }
            TestFolder.recursiveDelete(File("tmp"))
        }
        `when`("No experiments are included") {
            File("tmp").mkdir()
            File("tmp/build.gradle").createNewFile()
            val testLogger = TestPodLogger()
            val experimentController = ExperimentController("tmp", testLogger)
            experimentController.init()
            then("log message is registered with no messages") {
                assert(testLogger.containsLog("No experiments detected"))
            }
            TestFolder.recursiveDelete(File("tmp"))
        }
        `when`("Branch experimentation is detected") {
            File("tmp").mkdir()
            File("tmp/build.gradle").createNewFile()
            val testLogger = TestPodLogger()
            withEnvironment(mapOf("branch" to "develop")) {
                val experimentController = ExperimentController("tmp", testLogger)
                experimentController.init()
            }
            then("log message for branch experimentation is detected") {
                assert(testLogger.containsLog("Branch experimentation detected. Experiment applied at the Pod"))
            }
            TestFolder.recursiveDelete(File("tmp"))
        }
        `when`("Gradle Wrapper experimentation is detected") {
            File("tmp").mkdir()
            File("tmp/build.gradle").createNewFile()
            File("tmp/gradle").mkdir()
            File("tmp/gradle/wrapper").mkdir()
            File("tmp/gradle/wrapper/gradle-wrapper.properties").createNewFile()
            val testLogger = TestPodLogger()
            withEnvironment(mapOf("gradleWrapperVersion" to "5.5")) {
                val experimentController = ExperimentController("tmp", testLogger)
                experimentController.init()
            }
            then("log message for Gradle Wrapper experimentation is detected") {
                assert(testLogger.containsLog("Gradle Wrapper Versions experimentation detected."))
                assert(testLogger.containsLog("gradleWrapperVersion version value 5.5"))
            }
            TestFolder.recursiveDelete(File("tmp"))
        }
        `when`("Properties experimentation is detected") {
            File("tmp").mkdir()
            File("tmp/build.gradle").createNewFile()
            File("tmp/gradle.properties").createNewFile()
            val testLogger = TestPodLogger()
            withEnvironment(mapOf("properties" to "property=1")) {
                val experimentController = ExperimentController("tmp", testLogger)
                experimentController.init()
            }
            then("log message for Properties experimentation is detected") {
                assert(testLogger.containsLog("Gradle Properties experimentation detected."))
                assert(testLogger.containsLog("Begin process RewriteProperties"))
            }
            TestFolder.recursiveDelete(File("tmp"))

        }

    }

})
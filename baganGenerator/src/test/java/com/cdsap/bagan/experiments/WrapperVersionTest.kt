package com.cdsap.bagan.experiments

import com.cdsap.bagan.utils.TestFolder
import com.cdsap.bagan.utils.TestPodLogger
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.system.withEnvironment
import io.kotest.matchers.should
import io.kotest.matchers.string.haveSubstring
import io.kotest.matchers.string.shouldContain
import java.io.File
import java.io.FileNotFoundException


class WrapperVersionTest : BehaviorSpec({
    TestFolder.recursiveDelete(File("tmp"))
    given("GradleWrapperVersion instance") {
        `when`("Not environment variable is defined") {
            val testLogger = TestPodLogger()
            val wrapperVersion = GradleWrapperVersion("tmp", testLogger)
            wrapperVersion.applyExperiments()
            then("Error message is found with content not environment variable found") {
                assert(testLogger.containsLog("Error, not value found for the env variable gradleWrapperVersion"))
            }
        }
        `when`("Environment variable is found but not the file gradle-wrapper.properties") {
            val testLogger = TestPodLogger()
            val exception = shouldThrow<FileNotFoundException> {
                withEnvironment("gradleWrapperVersion" to "'5.1'") {
                    val wrapperVersion = GradleWrapperVersion("tmp", testLogger)
                    wrapperVersion.applyExperiments()

                }
            }
            then("exception is catched and log message is registered") {
                assert(testLogger.containsLog("gradle/wrapper/gradle-wrapper.properties not found"))
                exception.message shouldContain "gradle/wrapper/gradle-wrapper.properties not found"
            }
        }
        `when`("Environment variable and gradle-wrapper.properties are found") {
            val testLogger = TestPodLogger()
            File("tmp").mkdir()
            File("tmp/gradle").mkdir()
            File("tmp/gradle/wrapper").mkdir()
            File("tmp/gradle/wrapper/gradle-wrapper.properties").createNewFile()
            val contentFile = File("tmp/gradle/wrapper/gradle-wrapper.properties")
            contentFile.writeText("distributionUrl=https://services.gradle.org/distributions/gradle-5.4.1-bin.zip")

            withEnvironment("gradleWrapperVersion" to "'5.5'") {
                val wrapperVersion = GradleWrapperVersion("tmp", testLogger)
                wrapperVersion.applyExperiments()
            }
            then("Gradle wrapper properties file is updated with the new version") {
                val contentFile = File("tmp/gradle/wrapper/gradle-wrapper.properties")
                contentFile.readText() should haveSubstring("distributionUrl=https\\://services.gradle.org/distributions/gradle-5.5-bin.zip")
                assert(testLogger.containsLog("Writing property distributionUrl-https://services.gradle.org/distributions/gradle-5.5-bin.zip"))

            }
            TestFolder.recursiveDelete(File("tmp"))
        }
    }
})

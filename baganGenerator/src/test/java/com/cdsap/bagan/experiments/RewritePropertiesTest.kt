package com.cdsap.bagan.experiments

import com.cdsap.bagan.utils.TestFolder
import com.cdsap.bagan.utils.TestPodLogger
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.system.withEnvironment
import io.kotest.matchers.should
import io.kotest.matchers.shouldNot
import io.kotest.matchers.string.haveSubstring
import io.kotest.matchers.string.shouldContain
import java.io.File
import java.io.FileNotFoundException

class RewritePropertiesTest : BehaviorSpec({
    TestFolder.recursiveDelete(File("tmp"))
    given("RewriteProperties instance") {
        `when`("Not environment variable is defined") {
            val testLogger = TestPodLogger()
            val rewriteProperties = RewriteProperties("tmp", testLogger)
            rewriteProperties.applyExperiments()
            then("Error message is found with content not environment variable found") {
                assert(testLogger.containsLog("Error, not value found for the env variable properties"))
            }
        }
        `when`("Environment variable is found but not the file gradle.properties") {
            val testLogger = TestPodLogger()
            val exception = shouldThrow<FileNotFoundException> {
                withEnvironment("properties" to "4g") {
                    val rewriteProperties = RewriteProperties("tmp", testLogger)
                    rewriteProperties.applyExperiments()
                }
            }
            then("exception is catched and log message is registered") {
                assert(testLogger.containsLog("tmp/gradle.properties (No such file or directory)"))
                exception.message shouldContain "tmp/gradle.properties (No such file or directory)"
            }
        }
        `when`("Environment variable is found, gradle properties is found but not properties") {
            val testLogger = TestPodLogger()
            File("tmp").mkdir()
            File("tmp/gradle.properties").createNewFile()
            withEnvironment(
                "properties" to "org.gradle.jvmargs=-Xmx3g\n" +
                        "    org.gradle.caching=true"
            ) {
                val rewriteProperties = RewriteProperties("tmp", testLogger)
                rewriteProperties.applyExperiments()

            }
            then("Gradle properties files includes the new properties") {
                val contentFile = File("tmp/gradle.properties")
                contentFile.readText() should haveSubstring("org.gradle.jvmargs=-Xmx3g")
                contentFile.readText() should haveSubstring("org.gradle.caching=true")
                assert(testLogger.containsLog("Writing property org.gradle.jvmargs--Xmx3g"))
                assert(testLogger.containsLog("Writing property org.gradle.caching-true"))

            }
            TestFolder.recursiveDelete(File("tmp"))

        }
        `when`("Environment variable is found, gradle properties is found and properties are not include in the experiment") {
            val testLogger = TestPodLogger()
            File("tmp").mkdir()
            File("tmp/gradle.properties").createNewFile()
            val contentFile = File("tmp/gradle.properties")
            contentFile.writeText("property1=0")
            withEnvironment(
                "properties" to "org.gradle.jvmargs=-Xmx3g\n" +
                        "    org.gradle.caching=true"
            ) {
                val rewriteProperties = RewriteProperties("tmp", testLogger)
                rewriteProperties.applyExperiments()

            }
            then("Gradle properties files includes the new properties and keep the old one") {
                val contentFile = File("tmp/gradle.properties")
                contentFile.readText() should haveSubstring("org.gradle.jvmargs=-Xmx3g")
                contentFile.readText() should haveSubstring("org.gradle.caching=true")
                contentFile.readText() should haveSubstring("property1=0")
                assert(testLogger.containsLog("Writing property org.gradle.jvmargs--Xmx3g"))
                assert(testLogger.containsLog("Writing property org.gradle.caching-true"))

            }
            TestFolder.recursiveDelete(File("tmp"))
        }
        `when`("Environment variable is found, gradle properties is found and one of  properties exists in the experiment and file") {
            val testLogger = TestPodLogger()
            File("tmp").mkdir()
            File("tmp/gradle.properties").createNewFile()
            val contentFile = File("tmp/gradle.properties")
            contentFile.writeText("org.gradle.jvmargs=-Xmx2g")
            withEnvironment(
                "properties" to "org.gradle.jvmargs=-Xmx3g\n" +
                        "    org.gradle.caching=true"
            ) {
                val rewriteProperties = RewriteProperties("tmp", testLogger)
                rewriteProperties.applyExperiments()

            }
            then("Gradle properties files updates the value for the existing property") {
                val contentFile = File("tmp/gradle.properties")
                contentFile.readText() should haveSubstring("org.gradle.jvmargs=-Xmx3g")
                contentFile.readText() shouldNot haveSubstring("org.gradle.jvmargs=-Xmx2g")
                contentFile.readText() should haveSubstring("org.gradle.caching=true")
                assert(testLogger.containsLog("Writing property org.gradle.jvmargs--Xmx3g"))
                assert(testLogger.containsLog("Writing property org.gradle.caching-true"))

            }
            TestFolder.recursiveDelete(File("tmp"))
        }
    }
})


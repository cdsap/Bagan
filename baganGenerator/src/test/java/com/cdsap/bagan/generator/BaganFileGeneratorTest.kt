package com.cdsap.bagan.generator

import com.cdsap.bagan.utils.TestBagan
import com.cdsap.bagan.utils.TestExperiments
import com.cdsap.bagan.utils.TestFolder
import com.cdsap.bagan.utils.TestLogger
import io.kotlintest.matchers.haveSubstring
import io.kotlintest.should
import io.kotlintest.specs.BehaviorSpec
import java.io.File


class BaganFileGeneratorTest : BehaviorSpec({
    given("BaganFileGenerator instance") {
        `when`("generation with experiments provided") {
            File("tmp").mkdir()
            val testLogger = TestLogger()
            val baganFileGenerator = BaganFileGenerator(
                commandExecutor = CommandExecutor(testLogger, true),
                logger = testLogger,
                sessionExperiment = "sessionId",
                bagan = TestBagan.getBagan()
            )
            baganFileGenerator.generateExperiments(TestExperiments.getExperiments())
            then("file value are generated properly for experiment1") {
                val contentValuesFile = File("tmp/experiment1/values.yaml").readText()
                contentValuesFile should haveSubstring("branch: master")
                contentValuesFile should haveSubstring("name: experiment1")
                contentValuesFile should haveSubstring("session: sessionId")
                contentValuesFile should haveSubstring("cdsap/bagan-pod-injector:${Versions.POD_INJECTOR_VERSION}")

            }
            then("file chart are generated properly for experiment1") {
                val contentValuesFile = File("tmp/experiment1/chart.yaml").readText()
                contentValuesFile should haveSubstring("description: Experiment resources like Pod and Configmap required in Bagan.")
                contentValuesFile should haveSubstring("name: experiment1")
            }
            then("configmap is generated") {

                val contentValuesFile = File("tmp/experiment1/templates/configmapexperiment1.yaml").readText()
                contentValuesFile should haveSubstring("branch: master")
                contentValuesFile should haveSubstring("properties: |")
                contentValuesFile should haveSubstring("property1")
                contentValuesFile should haveSubstring("gradleWrapperVersion: '4.5'")

            }
            then("pod is generated with ssh options") {

                val contentValuesFile = File("tmp/experiment1/templates/podexperiment1.yaml").readText()
                contentValuesFile should haveSubstring(
                    "- name: GIT_SYNC_SSH\n" +
                            "      value: \"true\""
                )

            }
        }
        TestFolder.recursiveDelete(File("tmp"))
        `when`("Bagan Config File contains not private repository") {
            File("tmp").mkdir()

            val testLogger = TestLogger()
            val baganFileGenerator = BaganFileGenerator(
                commandExecutor = CommandExecutor(testLogger, true),
                logger = testLogger,
                sessionExperiment = "sessionId",
                bagan = TestBagan.getBaganNotPrivateRepo()
            )
            baganFileGenerator.generateExperiments(TestExperiments.getExperiments())
            then("file value are generated properly for experiment1") {
                val contentValuesFile = File("tmp/experiment1/templates/podexperiment1.yaml").readText()
                contentValuesFile should haveSubstring(
                    "- name: GIT_SYNC_SSH\n" +
                            "      value: \"false\""
                )
            }
        }
    //    TestFolder.recursiveDelete(File("tmp"))
    }

})

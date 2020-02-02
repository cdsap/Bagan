package com.cdsap.bagan.experiments

import com.cdsap.bagan.utils.TestFolder
import io.kotlintest.extensions.system.withEnvironment
import io.kotlintest.matchers.haveSubstring
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.BehaviorSpec
import java.io.File
//
//class ExecutionGeneratorTest : BehaviorSpec({
//    TestFolder.recursiveDelete(File("tmp"))
//    given("ExecutionGenerator Instance instance") {
//
//        `when`("All the environment variables are defined ") {
//            File("tmp").mkdir()
//            withEnvironment(
//                mapOf(
//                    "startingTask" to "./gradlew clean assembleInternal",
//                    "startingTaskIterations" to "2",
//                    "extraLabel" to "extraLabel",
//                    "taskExperimentation" to "./gradlew assemble",
//                    "iterationsExperiments" to "2",
//                    "files" to "modulea=filea\n" +
//                            "moduleb=fileb"
//                )
//            ) {
//                val executionGenerator = ExecutionGenerator("tmp")
//                executionGenerator.init()
//            }
//
//            then("file is generated following the criteria of initTask + change + experimentTask") {
//                val contentFile = File("tmp/execution.sh")
//                println(contentFile.readText())
//                contentFile.readText() should haveSubstring("for i in `seq 1 2`; do ./gradlew clean assembleInternal -PextraLabel=extraLabel ; done;")
//           //     contentFile.readText() should haveSubstring("sed '\$s/}/  fun myNewFunction0() {println(1) } }/' filea > temp.kt && mv temp.kt filea && rm temp.kt")
//                contentFile.readText() should haveSubstring("for i in `seq 1 2`; do ./gradlew assemble -PextraLabel=extraLabel ; done;")
//                contentFile.readText() should haveSubstring("for i in `seq 1 2`; do ./gradlew clean assembleInternal -PextraLabel=extraLabel ; done;")
//           //     contentFile.readText() should haveSubstring("sed '\$s/}/  fun myNewFunction1() {println(2) } }/' fileb > temp.kt && mv temp.kt fileb && rm temp.kt")
//
//            }
//            TestFolder.recursiveDelete(File("tmp"))
//        }
//        `when`("Taskexperimentation is missing") {
//            File("tmp").mkdir()
//
//
//            val exception = shouldThrow<Exception> {
//                withEnvironment(
//                    mapOf(
//                        "startingTask" to "./gradlew clean assembleInternal",
//                        "startingTaskIterations" to "2",
//                        "extraLabel" to "extraLabel",
//                        "iterationsExperiments" to "2",
//                        "files" to "modulea=filea\n" +
//                                "moduleb=fileb"
//                    )
//                ) {
//                    val executionGenerator = ExecutionGenerator("tmp")
//                    executionGenerator.init()
//                }
//            }
//            then("Exception is cached and message reflects missing TaskExperimentation") {
//                exception.message shouldBe "No taskExperiment value defined"
//            }
//
//        }
//        `when`("IterationsExperiment are missing") {
//            File("tmp").mkdir()
//
//
//            val exception = shouldThrow<Exception> {
//                withEnvironment(
//                    mapOf(
//                        "startingTask" to "./gradlew clean assembleInternal",
//                        "startingTaskIterations" to "2",
//                        "extraLabel" to "extraLabel",
//                        "taskExperimentation" to "./gradlew assemble",
//                        "files" to "modulea=filea\n" +
//                                "moduleb=fileb"
//                    )
//                ) {
//                    val executionGenerator = ExecutionGenerator("tmp")
//                    executionGenerator.init()
//                }
//            }
//            then("Exception is cached and message reflects missing IterationsExperiment") {
//                exception.message shouldBe "No iterationsExperiment value defined"
//            }
//
//        }
//        `when`("Files are missing") {
//            File("tmp").mkdir()
//
//
//            val exception = shouldThrow<Exception> {
//                withEnvironment(
//                    mapOf(
//                        "startingTask" to "./gradlew clean assembleInternal",
//                        "startingTaskIterations" to "2",
//                        "extraLabel" to "extraLabel",
//                        "taskExperimentation" to "./gradlew assemble",
//                        "iterationsExperiments" to "2"
//                    )
//                ) {
//                    val executionGenerator = ExecutionGenerator("tmp")
//                    executionGenerator.init()
//                }
//            }
//            then("Exception is cached and message reflects missing Files") {
//                exception.message shouldBe "No files defined to experiment"
//            }
//
//        }
//        `when`("ExtraLabel is missing") {
//            File("tmp").mkdir()
//
//
//            val exception = shouldThrow<Exception> {
//                withEnvironment(
//                    mapOf(
//                        "startingTask" to "./gradlew clean assembleInternal",
//                        "startingTaskIterations" to "2",
//                        "taskExperimentation" to "./gradlew assemble",
//                        "iterationsExperiments" to "2",
//                        "files" to "modulea=filea\n" +
//                                "moduleb=fileb"
//                    )
//                ) {
//                    val executionGenerator = ExecutionGenerator("tmp")
//                    executionGenerator.init()
//                }
//            }
//            then("Exception is cached and message reflects missing ExtraLabel") {
//                exception.message shouldBe "No extra label defined to experiment"
//            }
//
//        }
//        `when`("StatingTask is missing") {
//            File("tmp").mkdir()
//
//
//            val exception = shouldThrow<Exception> {
//                withEnvironment(
//                    mapOf(
//                        "startingTaskIterations" to "2",
//                        "extraLabel" to "extraLabel",
//                        "taskExperimentation" to "./gradlew assemble",
//                        "iterationsExperiments" to "2",
//                        "files" to "modulea=filea\n" +
//                                "moduleb=fileb"
//                    )
//                ) {
//                    val executionGenerator = ExecutionGenerator("tmp")
//                    executionGenerator.init()
//                }
//            }
//            then("Exception is cached and message reflects missing StatingTask") {
//                exception.message shouldBe "No starting Task defined to experiment"
//            }
//
//        }
//        `when`("StartingTaskIterations is missing") {
//            File("tmp").mkdir()
//
//
//            val exception = shouldThrow<Exception> {
//                withEnvironment(
//                    mapOf(
//                        "startingTask" to "./gradlew clean assembleInternal",
//                        "extraLabel" to "extraLabel",
//                        "taskExperimentation" to "./gradlew assemble",
//                        "iterationsExperiments" to "2",
//                        "files" to "modulea=filea\n" +
//                                "moduleb=fileb"
//                    )
//                ) {
//                    val executionGenerator = ExecutionGenerator("tmp")
//                    executionGenerator.init()
//                }
//            }
//            then("Exception is cached and message reflects missing StartingTaskIterations") {
//                exception.message shouldBe "No starting task iterations defined to experiment"
//            }
//
//        }
//    }
//
//})
//
//

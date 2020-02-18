package com.cdsap.bagan.generator

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.kotlintest.matchers.collections.shouldContain
import io.kotlintest.specs.BehaviorSpec

class ExperimentProviderTest : BehaviorSpec({
    given("ExperimentProvider Instance") {
        `when`("Simple Property is applied") {
            val baganConf = mock<BaganConfFileProvider>()
            whenever(baganConf.getBaganConf()).thenReturn(
                Bagan(
                    repository = "http ://git.com",
                    gradleCommand = "./gradlew assemble",
                    clusterName = "myCluster",
                    zone = "myZone",
                    project_id = "",
                    experiments = getSimpleExperiment(),
                    iterations = 10,
                    private = true
                )
            )
            val experimentProvider = ExperimentProvider(baganConf.getBaganConf())
            val experiments = experimentProvider.getExperiments()
            then("combinations for the simple experiment are generated") {
                experiments.containsAll(
                    listOf(
                        "gradlememory=1G",
                        "gradlememory=2G",
                        "gradlememory=3G",
                        "develop",
                        "master"
                    )
                )

            }
        }
        `when`("Multiple Property is applied") {
            val baganConf = mock<BaganConfFileProvider>()
            whenever(baganConf.getBaganConf()).thenReturn(
                Bagan(
                    repository = "http://git.com",
                    gradleCommand = "./gradlew assemble",
                    clusterName = "myCluster",
                    zone = "myZone",
                    project_id = "",
                    experiments = getMultipleExperiment(),
                    iterations = 10,
                    private = false
                )
            )
            val experimentProvider = ExperimentProvider(baganConf.getBaganConf())
            val experiments = experimentProvider.getExperiments()
            then("combinations for the Multiple experiment are generated") {
                assert(experiments.size == 18)
                experiments.containsAll(
                    listOf(
                        "gradlememory=1G}\n" +
                                "     caching=true}\n" +
                                "     workers=1",
                        "gradlememory=1G}\n" +
                                "     caching=false}\n" +
                                "     workers=1",
                        "gradlememory=1G}\n" +
                                "     caching=true}\n" +
                                "     workers=2",
                        "gradlememory=1G}\n" +
                                "     caching=false}\n" +
                                "     workers=2",
                        "gradlememory=1G}\n" +
                                "     caching=true}\n" +
                                "     workers=3",
                        "gradlememory=1G}\n" +
                                "     caching=false}\n" +
                                "     workers=3",
                        "gradlememory=2G}\n" +
                                "     caching=true}\n" +
                                "     workers=1",
                        "gradlememory=2G}\n" +
                                "     caching=false}\n" +
                                "     workers=1",
                        "gradlememory=2G}\n" +
                                "     caching=true}\n" +
                                "     workers=2",
                        "gradlememory=2G}\n" +
                                "     caching=false}\n" +
                                "     workers=2",
                        "gradlememory=2G}\n" +
                                "     caching=true}\n" +
                                "     workers=3",
                        "gradlememory=2G}\n" +
                                "     caching=false}\n" +
                                "     workers=3",
                        "gradlememory=3G}\n" +
                                "     caching=true}\n" +
                                "     workers=1",
                        "gradlememory=3G}\n" +
                                "     caching=false}\n" +
                                "     workers=1",
                        "gradlememory=3G}\n" +
                                "     caching=true}\n" +
                                "     workers=2",
                        "gradlememory=3G}\n" +
                                "     caching=false}\n" +
                                "     workers=2",
                        "gradlememory=3G}\n" +
                                "     caching=true}\n" +
                                "     workers=3",
                        "gradlememory=3G}\n" +
                                "     caching=false}\n" +
                                "     workers=3"

                    )
                )

            }

        }
    }
})

fun getSimpleExperiment() = Experiments(
    combined = CombinedExperiments(
        properties = arrayOf(
            Property(
                "gradlememory",
                arrayOf("1G", "2G", "3G")
            )
        ),
        branch = arrayOf("develop", "master ")
    )
)

fun getMultipleExperiment() = Experiments(
    combined = CombinedExperiments(
    properties = arrayOf(
        Property(
            "gradlememory",
            arrayOf("1G", "2G", "3G")
        ),
        Property(
            "caching",
            arrayOf("true", "false")
        ),
        Property(
            "workers",
            arrayOf("1", "2", "3")
        )
    )
))

fun getComposedExperiment() = Experiments(
    incrementalChanges = IncrementalChanges(
        taskExperiment = "./gradlew assemble",
        iterationsExperiment = 12,
        values = arrayOf(
            ComposeUnit(
                "branchA",
                files = arrayOf(
                    FileUnit("moduleA", "src/main/com/Second.kt"),
                    FileUnit("moduleB", "src/main/com/Third.kt")
                )
            ),
            ComposeUnit(
                branch = "branchB",
                files = arrayOf(
                    FileUnit("moduleA", "src/main/com/Second.kt"),
                    FileUnit("moduleB", "src/main/com/Third.kt")
                )
            )
        )
    )
)

fun baganWithMultipleExperiments() = Bagan(
    repository = "http ://git.com",
    gradleCommand = "./gradlew assemble",
    clusterName = "myCluster",
    zone = "myZone",
    project_id = "",
    iterations = 10,
    private = true,
    experiments = Experiments(
        CombinedExperiments(
        properties = arrayOf(
            Property("gradlememory", arrayOf("1G", "2G", "3G")),
            Property("caching", arrayOf("true", "false"))

        ),
        branch = arrayOf("develop", "master")
    )
))
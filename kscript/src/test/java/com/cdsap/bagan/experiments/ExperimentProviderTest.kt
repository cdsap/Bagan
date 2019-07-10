package com.cdsap.bagan.experiments

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.kotlintest.specs.BehaviorSpec

class ExperimentProviderTest : BehaviorSpec({
    given("ExperimentProvider Instance") {
        `when`("Simple Property is applied") {
            val baganConf = mock<BaganConfFileProvider>()
            whenever(baganConf.getBaganConf()).thenReturn(
                Bagan(
                    type = "gcloud",
                    repository = "http://git.com",
                    gradleCommand = "./gradlew assemble",
                    clusterName = "myCluster",
                    zone = "myZone",
                    project_id = "",
                    experiments = getSimpleExperiment(),
                    iterations = 10
                )
            )
            val experimentProvider = ExperimentProvider(baganConf)
            val experiments = experimentProvider.getExperiments()
            then("combinations for the simple experiment are generated") {
                experiments.containsAll(listOf("gradlememory=1G", "gradlememory=2G", "gradlememory=3G"))

            }
        }
        `when`("Multiple Property is applied") {
            val baganConf = mock<BaganConfFileProvider>()
            whenever(baganConf.getBaganConf()).thenReturn(
                Bagan(
                    type = "gcloud",
                    repository = "http://git.com",
                    gradleCommand = "./gradlew assemble",
                    clusterName = "myCluster",
                    zone = "myZone",
                    project_id = "",
                    experiments = getMultipleExperiment(),
                    iterations = 10
                )
            )
            val experimentProvider = ExperimentProvider(baganConf)
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

fun getSimpleExperiment() = GradleExperimentsProperties(
    properties = arrayOf(
        Property(
            "gradlememory",
            arrayOf("1G", "2G", "3G")
        )
    )
)

fun getMultipleExperiment() = GradleExperimentsProperties(
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
)


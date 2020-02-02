package com.cdsap.bagan.utils

import com.cdsap.bagan.generator.*

object TestBagan {

    fun getBagan() = Bagan(
        repository = "http://git.com",
        gradleCommand = "./gradlew assemble",
        clusterName = "myCluster",
        zone = "myZone",
        project_id = "",
        experiments = getSimpleExperiment(),
        iterations = 10,
        private = true
    )

    fun getBaganNotPrivateRepo() = Bagan(
        repository = "http://git.com",
        gradleCommand = "./gradlew assemble",
        clusterName = "myCluster",
        zone = "myZone",
        project_id = "",
        experiments = getSimpleExperiment(),
        iterations = 10,
        private = false
    )

    fun getBaganMultipleCommands() = Bagan(
        repository = "http://git.com",
        gradleCommand = "./gradlew clean assemble test",
        clusterName = "myCluster",
        zone = "myZone",
        project_id = "",
        experiments = getSimpleExperiment(),
        iterations = 10,
        private = true
    )

    fun getBaganWithOnlyCleanCommand() = Bagan(
        repository = "http://git.com",
        gradleCommand = "./gradlew clean",
        clusterName = "myCluster",
        zone = "myZone",
        project_id = "",
        experiments = getSimpleExperiment(),
        iterations = 10,
        private = true
    )

    fun getBaganWithComposedExperiments() = Bagan(
        repository = "http://git.com",
        gradleCommand = "./gradlew clean",
        clusterName = "myCluster",
        zone = "myZone",
        project_id = "",
        experiments = getComposedExperiment(),
        iterations = 10,
        private = true
    )

    private fun getComposedExperiment() = GradleExperimentsProperties(
        compose = Compose(
            taskExperiment = "./gradlew assemble",
            iterationsExperiment = 12,
            values = arrayOf(
                ComposeUnit(
                    branch = "branchA",
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
}
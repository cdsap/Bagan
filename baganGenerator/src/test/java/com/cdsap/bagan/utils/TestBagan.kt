package com.cdsap.bagan.utils

import com.cdsap.bagan.generator.Bagan
import com.cdsap.bagan.generator.getSimpleExperiment

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
}
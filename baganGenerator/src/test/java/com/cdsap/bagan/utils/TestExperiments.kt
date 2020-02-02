package com.cdsap.bagan.utils

import com.cdsap.bagan.generator.ComposeExperiment
import com.cdsap.bagan.generator.Experiment
import com.cdsap.bagan.generator.FileUnit
import com.cdsap.bagan.generator.UnitExperiment

object TestExperiments {
    fun getExperiments(): List<Experiment> {
        val experiments = listOf(
            Experiment("property1", "master", "4.5"),
            Experiment("property2", "develop", "5.1")
        )
        var counter = 1
        experiments.forEach {
            it.name = "experiment" + counter++
        }

        return experiments
    }

    fun getComposedExperiments(): List<Experiment> {
        val experiment1 = Experiment(
            properties = "",
            gradleWrapperVersion = "",
            branch = ""
        )
        experiment1.composeExperiment = ComposeExperiment(
            taskExperiment = "./gradlew assembleInternal",
            iterationsExperiments = 12,
            branch = "branchA",
            files = listOf(
                UnitExperiment("moduleA", "fileA"),
                UnitExperiment("moduleB", "fileB")
            )
        )
        val experiment2 = Experiment(
            properties = "",
            gradleWrapperVersion = "",
            branch = ""
        )
        experiment2.composeExperiment = ComposeExperiment(
            taskExperiment = "./gradlew assembleInternal",
            iterationsExperiments = 12,
            branch = "branchB",
            files = listOf(
                UnitExperiment("moduleA", "fileA"),
                UnitExperiment("moduleB", "fileB")
            )
        )
        val experiment = listOf(experiment1, experiment2)

        var counter = 1
        experiment.forEach {
            it.name = "experiment" + counter++
        }
        return experiment
    }
}
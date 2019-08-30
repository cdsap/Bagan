package com.cdsap.bagan.utils

import com.cdsap.bagan.generator.Experiment

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
}
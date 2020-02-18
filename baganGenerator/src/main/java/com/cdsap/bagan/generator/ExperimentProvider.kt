package com.cdsap.bagan.generator

import kotlin.reflect.KFunction

/**
 * Class to generate list of experiments given [Bagan] configuration.
 * Currently, we have three different types of experiments:
 *  * Properties on file gradle.properties
 *  * Branch
 *  * Gradle wrapper version
 *
 *  We need to provide a combination of all experiments included in the configuration file, we will achieve applying first
 *  permutations for all the properties experiments. Then we will apply a cartesian product for the different
 *  sets(proeprties, branches, gradle wrapper versions)
 *
 */

class ExperimentProvider(private val bagan: Bagan) {


    fun getExperiments(): List<Experiment> {
        if (bagan.experiments.incrementalChanges != null) {
            var counter = 1
            val experiments = mutableListOf<Experiment>()
            bagan.experiments.incrementalChanges.values.forEach {
                val elements = it.files.flatMap {
                    listOf(UnitExperiment(it.module, it.path))
                }
                val experiment = Experiment(
                    properties = "",
                    branch = "",
                    gradleWrapperVersion = ""
                )
                experiment.composeExperiment = ComposeExperiment(
                    taskExperiment = bagan.experiments.incrementalChanges.taskExperiment,
                    iterationsExperiments = bagan.experiments.incrementalChanges.iterationsExperiment,
                    branch = it.branch,
                    files = elements
                )

                experiment.name = "experiment$counter"
                counter++
                experiments.add(experiment)
            }
            return experiments

        } else {
            val experimentsProperties = getExperimentsProperties()?.toSet() ?: setOf("")
            val experimentBranches = bagan.experiments.combined?.branch?.toSet() ?: setOf("")
            val experimentGradleWrapper = bagan.experiments.combined?.gradleWrapperVersion?.toSet() ?: setOf("")

            val listExperiments = cartesianProduct(
                experimentsProperties,
                experimentBranches,
                experimentGradleWrapper
            ).map(::Experiment)

            var count = 1
            listExperiments.forEach {
                it.name = "experiment" + count++
            }

            return listExperiments
        }
    }

    private fun getExperimentsProperties(): List<String>? =
        if (bagan.experiments.combined?.properties != null) {
            val experiments = mutableListOf<List<String>>()

            bagan.experiments.combined.properties?.forEach {
                val experiment = mutableListOf<String>()
                val prefix = it.name
                it.options.forEach {
                    experiment.add("$prefix=$it")
                }
                experiments.add(experiment)
            }
            getExperimentsPermutations(experiments)
        } else {
            null
        }

    private fun generatePermutations(
        lists: MutableList<List<String>>,
        result: MutableList<String>,
        depth: Int,
        current: String
    ) {
        if (depth == lists.size) {
            result.add(current)
            return
        }

        for (i in 0 until lists[depth].size) {
            generatePermutations(lists, result, depth + 1, current + "${lists[depth][i]}\n               ")
        }
    }

    private fun getExperimentsPermutations(experiments: MutableList<List<String>>): List<String> {
        val experimentsPermuted = mutableListOf<String>()
        generatePermutations(experiments, experimentsPermuted, 0, "")
        return experimentsPermuted
    }


    fun cartesianProduct(vararg sets: Set<*>): Set<List<*>> =
        when (sets.size) {
            0, 1 -> emptySet()
            else -> sets.fold(listOf(listOf<Any?>())) { acc, set ->
                acc.flatMap { list -> set.map { element -> list + element } }
            }.toSet()
        }

    fun <T> Set<List<*>>.map(transform: KFunction<T>) = map { transform.call(*it.toTypedArray()) }

}

data class Experiment(
    val properties: String,
    val branch: String,
    val gradleWrapperVersion: String
) {
    var name: String = ""
    var composeExperiment: ComposeExperiment? = null
}

data class ComposeExperiment(
    val taskExperiment: String,
    val iterationsExperiments: Int,
    val branch: String, val files: List<UnitExperiment>
)

data class UnitExperiment(val module: String, val path: String)
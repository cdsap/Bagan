package com.cdsap.bagan.experiments

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths


class ExperimentProvider(private val moshiProvider: MoshiProvider) {

    val bagan by lazy {
        val jsonAdapter = moshiProvider.adapter(BaganJson::class.java)

        val baganJson: BaganJson =
            jsonAdapter.fromJson(
                String(Files.readAllBytes(Paths.get("${Versions.PATH}bagan_conf.json")), StandardCharsets.US_ASCII)
            ) ?: throw Exception("Error parsing json file")
        baganJson.bagan
    }

    fun getExperiments(): List<String> = getExperiments(bagan)

    private fun getExperiments(bagan: Bagan): List<String> {

        val experiments = mutableListOf<List<String>>()

        bagan.experiments.properties.forEach {
            val experiment = mutableListOf<String>()
            val prefix = it.name
            it.options.forEach {
                experiment.add("$prefix=$it")
            }
            experiments.add(experiment)
        }
        return getExperimentsPermutations(experiments)
    }


    private fun getExperimentsPermutations(experiments: MutableList<List<String>>): List<String> {
        val experimentsPermuted = mutableListOf<String>()
        generatePermutations(experiments, experimentsPermuted, 0, "")
        return experimentsPermuted
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
            generatePermutations(lists, result, depth + 1, current + "${lists[depth][i]}\n     ")
        }
    }

}
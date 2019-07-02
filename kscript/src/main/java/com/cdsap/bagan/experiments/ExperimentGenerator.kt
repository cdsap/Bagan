package com.cdsap.bagan.experiments


class ExperimentGenerator(val bagan: Bagan) {


    private fun getExperiments(bagan: Bagan): MutableList<List<String>> {
        val experiments = mutableListOf<List<String>>()

        bagan.experiments.properties.forEach {
            val experiment = mutableListOf<String>()
            val prefix = it.name
            it.options.forEach {
                experiment.add("$prefix=$it")
            }
            experiments.add(experiment)
        }
        return experiments
    }


    fun getExperiments(): List<String> {
        val experimentsPermuted = mutableListOf<String>()
        generatePermutations(getExperiments(bagan), experimentsPermuted, 0, "")
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
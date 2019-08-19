package com.cdsap.bagan.experiments

class ExperimentProvider(private val bagan: Bagan) {


    fun getExperiments(): List<String> {

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
            generatePermutations(lists, result, depth + 1, current + "${lists[depth][i]}\n               ")
        }
    }

}

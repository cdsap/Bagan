package com.cdsap.monitor.repository

import com.cdsap.monitor.data.MySqlApi
import com.cdsap.monitor.entities.domain.Experiment


class ExperimentRepositoryImpl(private val mysql: MySqlApi) : ExperimentRepository {

    override fun insertExperiment(experiment: String): String {
        val a = mysql.insertExperiment(experiment)
        return a.statusMessage ?: "OK"
    }

    override fun getExperiments(): List<Experiment> {
        val experiments = mutableListOf<Experiment>()
        val experimentsQuery = mysql.getExperiments()
        experimentsQuery.rows.forEach {
            experiments.add(Experiment(it.getString("NAME").toString()))
        }
        return experiments
    }
}
package com.cdsap.monitor.repository

import com.cdsap.monitor.entities.domain.Experiment


interface ExperimentRepository {
    fun insertExperiment(experiment: String): String

    fun getExperiments(): List<Experiment>

}
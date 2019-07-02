package com.cdsap.monitor.data

import com.cdsap.monitor.entities.domain.Pod
import com.github.jasync.sql.db.QueryResult

interface MySqlApi {
    fun insertPod(pod: Pod): QueryResult

    fun insertExperiment(name: String): QueryResult

    fun updatePodCounter(pod: Pod): QueryResult

    fun getExperiments(): QueryResult

    fun getPodsByExperiment(name: String): QueryResult

    fun getPodByExperimentAndName(experiment: String, name: String): QueryResult
}
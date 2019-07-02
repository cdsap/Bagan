package com.cdsap.monitor.repository

import com.cdsap.monitor.data.MySqlApi
import com.cdsap.monitor.entities.domain.Pod
import com.cdsap.monitor.entities.request.PodRequest

class PodRepositoryImpl(private val mysql: MySqlApi) : PodRepository {

    override fun insertPod(experiment: String, pod: String, xRequest: PodRequest): String {
        val a = mysql.insertPod(
                Pod(name = pod,
                        experiment_id = experiment,
                        values = xRequest.values,
                        configMap = xRequest.configMap,
                        iterations = xRequest.iterations)
        )
        return a.statusMessage ?: "OK"
    }

    override fun updateCounterPod(experiment: String, pod: String): String {
        val currentCounter = mysql.getPodByExperimentAndName(experiment, pod)
        return if (currentCounter.rowsAffected == 1L) {
            val counter = currentCounter.rows[0][0] as Int - 1
            val a = mysql.updatePodCounter(
                    Pod(name = pod,
                            experiment_id = experiment,
                            iterations = counter
                    )
            )
            a.statusMessage ?: "OK"
        } else {
            "error retrieving information about pod"
        }
    }

    override fun getPodsByExperiment(experiment: String): List<Pod> {
        val pods = mutableListOf<Pod>()
        val podsByExperiment = mysql.getPodsByExperiment(experiment)
        podsByExperiment.rows.forEach {
            pods.add(
                    Pod(
                            name = it.getString("NAME").toString(),
                            experiment_id = it.getString("EXPERIMENT_ID").toString(),
                            iterations = it.getInt("COUNTER")!!.toInt(),
                            values = it.getString("EXPERIMENTS").toString()
                    )
            )
        }
        return pods
    }
}
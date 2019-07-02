package com.cdsap.monitor.repository

import com.cdsap.monitor.entities.domain.Pod
import com.cdsap.monitor.entities.request.PodRequest

interface PodRepository {
    fun insertPod(experiment: String, pod: String, xRequest: PodRequest): String

    fun updateCounterPod(experiment: String, pod: String): String

    fun getPodsByExperiment(experiment: String): List<Pod>
}
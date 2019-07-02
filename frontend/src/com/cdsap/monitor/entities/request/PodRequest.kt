package com.cdsap.monitor.entities.request

data class PodRequest(
        val values: String,
        val configMap: String,
        val iterations: Int
)
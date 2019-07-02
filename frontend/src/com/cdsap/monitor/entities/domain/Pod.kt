package com.cdsap.monitor.entities.domain

data class Pod(
        val name: String,
        val experiment_id: String,
        val values: String = "",
        val configMap: String = "",
        val iterations: Int
)
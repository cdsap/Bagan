package com.cdsap.bagan.experiments

import com.squareup.moshi.Json

data class Bagan(
    val type: String,
    val repository: String,
    val gradleCommand: String,
    val clusterName: String?,
    val zone: String?,
    @Json(name = "project-id") val project_id: String?,
    val experiments: GradleExperimentsProperties,
    val iterations: Int
)

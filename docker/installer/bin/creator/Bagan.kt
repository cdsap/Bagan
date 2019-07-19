//DEPS com.squareup.moshi:moshi-kotlin:1.8.0


package com.cdsap.bagan.experiments

import com.squareup.moshi.Json

data class Bagan(
    val repository: String,
    val gradleCommand: String,
    val clusterName: String?,
    val zone: String?,
    @Json(name = "project-id") val project_id: String?,
    val experiments: GradleExperimentsProperties,
    val iterations: Int
)

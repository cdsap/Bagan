package com.cdsap.bagan.experiments

import com.squareup.moshi.Json

data class Bagan(
    val repository: String,
    val gradleCommand: String,
    val clusterName: String?,
    val zone: String?,
    @Json(name = "project-id") val project_id: String?,
    val experiments: GradleExperimentsProperties,
    val iterations: Int,
    val private: Boolean
)

data class BaganJson(val bagan: Bagan)

data class Experiment(val name: String, val values: String)

data class GradleExperimentsProperties(
    val properties: Array<Property>,
    val agp: Array<String>? = null,
    val gradleVersion: Array<String>? = null
)

data class Property(val name: String, val options: Array<String>)
package com.cdsap.bagan.generator

import com.squareup.moshi.Json

data class Bagan(
    val repository: String,
    val gradleCommand: String,
    val clusterName: String?,
    val zone: String?,
    @Json(name = "project-id") val project_id: String?,
    val experiments: GradleExperimentsProperties,
    val iterations: Int,
    val private: Boolean,
    val talaiot: Talaiot? = Talaiot()
)

data class BaganJson(val bagan: Bagan)

data class GradleExperimentsProperties(
    val properties: Array<Property>? = null,
    val branch: Array<String>? = null,
    val gradleWrapperVersion: Array<String>? = null
)

data class Property(val name: String, val options: Array<String>)

data class Talaiot(
    val publishTaskMetrics: Boolean = true,
    val publishBuildMetrics: Boolean = true
)

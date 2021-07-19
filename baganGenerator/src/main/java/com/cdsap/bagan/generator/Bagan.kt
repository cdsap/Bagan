package com.cdsap.bagan.generator

import com.squareup.moshi.Json
import java.io.File

data class Bagan(
    val repository: String,
    val clusterName: String? = null,
    val zone: String? = null,
    @Json(name = "project-id") val project_id: String? = null,
    val gradleCommand: String,
    val experiments: GradleExperimentsProperties,
    val iterations: Int = 10,
    val warmups: Int = 2,
    val private: Boolean,
    val scenarioFile: File? = null,
    val scenarioName: String? = null
)

data class BaganJson(val bagan: Bagan)

data class GradleExperimentsProperties(
    val properties: Array<Property>? = null,
    val branch: Array<String>? = null,
    val gradleWrapperVersion: Array<String>? = null
)

data class Property(val name: String, val options: Array<String>)

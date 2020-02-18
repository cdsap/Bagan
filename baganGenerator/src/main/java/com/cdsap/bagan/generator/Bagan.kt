package com.cdsap.bagan.generator

import com.squareup.moshi.Json

data class Bagan(
    val repository: String,
    val gradleCommand: String,
    val clusterName: String?,
    val zone: String?,
    @Json(name = "project-id") val project_id: String?,
    val experiments: Experiments,
    val iterations: Int,
    val private: Boolean
)

data class BaganJson(val bagan: Bagan)

data class Experiments(
    val combined: CombinedExperiments? = null,
    val incrementalChanges: IncrementalChanges? = null
)

data class CombinedExperiments(
    val properties: Array<Property>? = null,
    val branch: Array<String>? = null,
    val gradleWrapperVersion: Array<String>? = null
)

data class Property(val name: String, val options: Array<String>)


data class IncrementalChanges(
    val taskExperiment: String,
    val iterationsExperiment: Int,
    val values: Array<ComposeUnit>
)

data class ComposeUnit(
    val branch: String,
    val files: Array<FileUnit>
)

data class FileUnit(val module: String, val path: String)
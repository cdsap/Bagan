package com.cdsap.bagan.experiments


data class GradleExperimentsProperties(
    val properties: Array<Property>,
    val agp: Array<String>? = null,
    val gradleVersion: Array<String>? = null
)



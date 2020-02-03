package com.cdsap.bagan.experiments

data class IncrementalChangeInfo(
    val initialTask: String,
    val initialTaskIterations: Int,
    val taskExperimentation: String,
    val taskExperimentationIterations: Int,
    val extraLabel: String
)

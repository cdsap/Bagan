package com.cdsap.bagan.experiments


fun main() {
    ExperimentController(".", LoggerPodImpl()).init()
}

class ExperimentController(private val path: String, private val logger: LoggerPod) {
    private val TAG = "ExperimentController"

    fun init() {
        logger.log(TAG, "Staring the process")
        injectTalaiot()
        applyExperiment()
    }

    private fun injectTalaiot() {
        TalaiotInjector(path, logger).init()
    }

    private fun applyExperiment() {
        val properties = System.getenv("properties")
        val branch = System.getenv("branch")
        val gradleWrapperVersion = System.getenv("gradleWrapperVersion")

        if (properties != null) {
            logger.log(TAG, "Gradle Properties experimentation detected.")
            RewriteProperties(path, logger).applyExperiments()
        }

        if (branch != null) {
            logger.log(TAG, "Branch experimentation detected. Experiment applied at the Pod")

        }

        if (gradleWrapperVersion != null) {
            logger.log(TAG, "Gradle Wrapper Versions experimentation detected.")
            GradleWrapperVersion(path, logger).applyExperiments()
        }

        if (properties == null && branch == null && gradleWrapperVersion == null) {
            logger.log(TAG, "No experiments detected")
        }
    }
}
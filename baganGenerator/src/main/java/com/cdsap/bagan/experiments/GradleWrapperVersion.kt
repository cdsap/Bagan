package com.cdsap.bagan.experiments

import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.util.*

class GradleWrapperVersion(
    private val path: String,
    private val logger: LoggerPod
) : ExperimentPod {
    private val TAG = "GradleWrapperVersion"
    override fun applyExperiments() {
        logger.log(TAG, "Begin process")

        val value = System.getenv("gradleWrapperVersion")
        if (value == null) {
            logger.log(TAG, "Error, not value found for the env variable gradleWrapperVersion")
        } else {
            logger.log(TAG, "gradleWrapperVersion version value $value")
            val url = "https://services.gradle.org/distributions/gradle-$value-bin.zip"
            try {
                val inProperties = FileInputStream("$path/gradle/wrapper/gradle-wrapper.properties")
                val props = Properties()
                props.load(inProperties)
                inProperties.close()

                val outProperties = FileOutputStream("$path/gradle/wrapper/gradle-wrapper.properties")
                logger.log(TAG, "Writing property distributionUrl-$url")
                props.setProperty("distributionUrl", url)
                props.store(outProperties, null)
                outProperties.close()
                logger.log(TAG, "End process")
            } catch (e: FileNotFoundException) {
                logger.log(TAG, "gradle/wrapper/gradle-wrapper.properties not found")
                throw FileNotFoundException("gradle/wrapper/gradle-wrapper.properties not found")
            }
        }
    }
}
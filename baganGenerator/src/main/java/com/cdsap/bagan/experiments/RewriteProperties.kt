package com.cdsap.bagan.experiments


import java.io.*
import java.util.*

class RewriteProperties(
    private val path: String,
    private val logger: LoggerPod
) : ExperimentPod {
    private val TAG = "RewriteProperties"

    override fun applyExperiments() {
        logger.log(TAG, "Begin process RewriteProperties")
        if (System.getenv("properties") == null) {
            logger.log(TAG, "Error, not value found for the env variable properties")

        } else {
            val properties = StringReader(System.getenv("properties"))
            try {
                val newProps = Properties()
                newProps.load(properties)

                val inProperties = FileInputStream("$path/gradle.properties")
                val props = Properties()
                props.load(inProperties)
                inProperties.close()

                val outProperties = FileOutputStream("$path/gradle.properties")
                newProps.forEach {
                    logger.log(TAG, "Writing property ${it.key}-${it.value}")
                    props.setProperty(it.key.toString(), it.value.toString())
                }
                props.store(outProperties, null)
                outProperties.close()
                logger.log(TAG, "End process")
            } catch (e: FileNotFoundException) {
                logger.log(TAG, "tmp/gradle.properties (No such file or directory)")
                throw FileNotFoundException("tmp/gradle.properties (No such file or directory)")
            }
        }
    }
}


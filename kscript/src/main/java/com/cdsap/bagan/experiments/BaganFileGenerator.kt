package com.cdsap.bagan.experiments

import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class BaganFileGenerator(
    private val sessionExperiment: String,
    private val monitorReporting: MonitorReporting,
    private val logger: Logger
) {

    fun createExperiment(experiment: String, values: String, bagan: Bagan) {
        val nameConfigMap = "configmap$experiment"
        val namePod = "$experiment/templates/pod$experiment.yaml"

        createFolder(experiment)
        createChartFile("$experiment/Chart.yaml", experiment)
        createFileValues(
            "$experiment/values.yaml",
            bagan.repository,
            bagan.gradleCommand,
            nameConfigMap,
            bagan.iterations,
            experiment
        )

        createTemplateFolder(experiment)
        createConfigMaps(experiment, nameConfigMap, values)
        createPods(namePod, experiment, sessionExperiment)

        monitorReporting.insertPod(
            values = values.replace("\n     ", "\n"),
            iterations = bagan.iterations,
            configMap = nameConfigMap,
            experiment = sessionExperiment,
            pod = namePod
        )

    }

    private fun createChartFile(path: String, id: String) {
        logger.log("creating Chart file $path")
        val file = File(path)
        file.writeText(Chart().transform(id, Versions.CURRENT_VERSION))
    }

    private fun createFolder(path: String) {
        logger.log("creating  folder $path")
        removeExistingFolder(path)
        Files.createDirectory(Paths.get(path))
    }

    private fun removeExistingFolder(path: String) {
        if (Files.exists(Paths.get(path))) {
            logger.log("removing existing experiment folder $path")
            val file = File(path)
            file.deleteRecursively()
        }
    }

    private fun createFileValues(
        path: String,
        nameRepo: String,
        gradleCommand: String,
        s2: String,
        iterations: Int,
        nameExperiment: String
    ) {
        logger.log("creating Values file $path")
        val file = File(path)
        file.writeText(Values().transform(nameRepo, s2, nameExperiment, gradleCommand, iterations))
    }

    private fun createTemplateFolder(path: String) {
        logger.log("creating template folder $path")
        Files.createDirectory(Paths.get("$path/templates"))
    }

    private fun createConfigMaps(
        nameExperiment: String,
        s1: String,
        propertyName: String

    ) {
        logger.log("creating configmap file $nameExperiment/templates/$s1.yaml")
        val file = File("$nameExperiment/templates/$s1.yaml")
        file.writeText(ConfigMap().transform(nameExperiment, s1, propertyName))
    }

    private fun createPods(path: String, nameExperiment: String, experiment: String) {
        logger.log("creating pod file $path")
        val file = File(path)
        file.writeText(Pod().transform(nameExperiment, experiment))
    }
}

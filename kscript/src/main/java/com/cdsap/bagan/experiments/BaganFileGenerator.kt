package com.cdsap.bagan.experiments

import com.cdsap.bagan.experiments.Versions.TEMP_FOLDER
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class BaganFileGenerator(
    private val sessionExperiment: String,
    private val logger: Logger,
    private val commandExecutor: CommandExecutor
) {
    val TAG = "BaganFileGenerator"
    fun createExperiment(experiment: String, values: String, bagan: Bagan) {
        val path = "$TEMP_FOLDER/$experiment"
        val nameConfigMap = "$path/templates/configmap$experiment.yaml"
        val namePod = "$path/templates/pod$experiment.yaml"
        logger.log(TAG, "Experiment $experiment")
        createFolder(path)
        createChartFile("$path/Chart.yaml", experiment)
        createFileValues(
            "$path/values.yaml",
            bagan.repository,
            bagan.gradleCommand,
            nameConfigMap,
            bagan.iterations,
            experiment
        )

        createTemplateFolder(path)
        createPods(namePod, experiment, sessionExperiment)
        createConfigMaps(nameConfigMap, experiment, values)
        commandExecutor.execute("helm install -n $experiment -f $path/values.yaml $path/")
    }

    private fun createChartFile(path: String, id: String) {
        logger.log(TAG, "creating Chart file $path")
        val file = File(path)
        file.writeText(Chart().transform(id, Versions.CURRENT_VERSION))
    }

    private fun createFolder(path: String) {
        logger.log(TAG, "creating  folder $path")
        removeExistingFolder(path)
        Files.createDirectory(Paths.get(path))
    }

    private fun removeExistingFolder(path: String) {
        if (Files.exists(Paths.get(path))) {
            logger.log(TAG, "removing existing experiment folder $path")
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
        logger.log(TAG, "creating Values file $path")
        val file = File(path)
        file.writeText(Values().transform(nameRepo, s2, nameExperiment, gradleCommand, iterations))
    }

    private fun createTemplateFolder(path: String) {
        logger.log(TAG, "creating template folder $path")
        Files.createDirectory(Paths.get("$path/templates"))
    }

    private fun createConfigMaps(
        nameConfigMap: String,
        experiment: String,
        propertyName: String

    ) {
        logger.log(TAG, "creating configmap file $nameConfigMap")
        val file = File(nameConfigMap)
        file.writeText(ConfigMap().transform(experiment, propertyName))
    }

    private fun createPods(path: String, nameExperiment: String, experiment: String) {
        logger.log(TAG, "creating pod file $path")
        val file = File(path)
        file.writeText(Pod().transform(nameExperiment, experiment))
    }
}

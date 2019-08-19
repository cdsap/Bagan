package com.cdsap.bagan.experiments

import com.cdsap.bagan.experiments.Versions.TEMP_FOLDER
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class BaganFileGenerator(
    private val sessionExperiment: String,
    private val bagan: Bagan,
    private val logger: Logger,
    private val commandExecutor: CommandExecutor
) {
    val TAG = "BaganFileGenerator"


    fun generateExperiments(experiments: List<Experiment>) {
        logger.log(TAG, "Starting DashboardProvider")

        experiments.forEach {
            createExperiment(it.name, it.values)
        }
    }

    private fun createExperiment(experiment: String, values: String) {
        val path = "$TEMP_FOLDER/$experiment"

        logger.log(TAG, "Experiment $experiment")

        createFolder(
            path = path
        )

        createFileValues(
            path = "$path/values.yaml",
            bagan = bagan,
            nameExperiment = experiment,
            image = Versions.POD_INJECTOR,
            session = sessionExperiment
        )

        createChartFile(
            path = "$path/Chart.yaml",
            id = experiment
        )

        createTemplateFolder(
            path = path
        )

        createPods(
            path = "$path/templates/pod$experiment.yaml",
            privateRepo = bagan.private
        )

        createConfigMaps(
            path = "$path/templates/configmap$experiment.yaml",
            properties = values
        )

        // commandExecutor.execute("helm install -n $experiment -f $path/values.yaml $path/")
    }

    private fun createChartFile(path: String, id: String) {
        logger.log(TAG, "creating Chart file $path")
        val file = File(path)
        file.writeText(Chart().transform(id, Versions.CHART_EXPERIMENT))
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
        bagan: Bagan,
        nameExperiment: String,
        image: String,
        session: String
    ) {
        logger.log(TAG, "creating Values file $path")
        val file = File(path)
        file.writeText(
            Values().transform(
                repository = bagan.repository,
                name = nameExperiment,
                command = bagan.gradleCommand,
                iterations = bagan.iterations,
                image = image,
                session = session
            )
        )
    }

    private fun createTemplateFolder(path: String) {
        logger.log(TAG, "creating template folder $path")
        Files.createDirectory(Paths.get("$path/templates"))
    }

    private fun createConfigMaps(
        path: String,
        properties: String

    ) {
        logger.log(TAG, "creating configmap file $path")
        val file = File(path)
        file.writeText(ConfigMap().transform(properties))
    }

    private fun createPods(
        path: String,
        privateRepo: Boolean
    ) {
        logger.log(TAG, "creating pod file $path")
        val file = File(path)
        if (privateRepo) {
            file.writeText(PodSecure().transform())
        } else {
            file.writeText(Pod().transform())
        }
    }

}

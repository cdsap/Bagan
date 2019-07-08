package com.cdsap.bagan.experiments

import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class BaganFileGenerator(
    private val sessionExperiment: String,
    private val monitorReporting: MonitorReporting
) {

    fun createExperiment(experiment: String, values: String, bagan: Bagan) {
        val nameConfigMap = "configmap$experiment"
        val namePod = "$experiment/templates/pod$experiment.yaml"
        monitorReporting.insertPod(
            values = values.replace("\n     ", "\n"),
            iterations = bagan.iterations,
            configMap = nameConfigMap,
            experiment = sessionExperiment,
            pod = namePod
        )

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
        createPods(
            namePod, experiment, sessionExperiment
        )


    }

    private fun createChartFile(path: String, id: String) {
        println("creating chart file ")
        val file = File(path)
        file.writeText(Chart().transform(id, Versions.CURRENT_VERSION))
    }

    private fun createFolder(path: String) {
        println("creating folder")
        Files.createDirectory(Paths.get(path))
    }

    private fun createFileValues(
        path: String,
        nameRepo: String,
        gradleCommand: String,
        s2: String,
        iterations: Int,
        nameExperiment: String
    ) {
        val file = File(path)
        println("creating fileValues")
        file.writeText(Values().transform(nameRepo, s2, nameExperiment, gradleCommand, iterations))
    }

    private fun createTemplateFolder(path: String) {
        println("creating template folder")
        Files.createDirectory(Paths.get("$path/templates"))
    }

    private fun createConfigMaps(
        nameExperiment: String,
        s1: String,
        propertyName: String

    ) {
        println("creating configMpas")
        val file = File("$nameExperiment/templates/$s1.yaml")
        file.writeText(ConfigMap().transform(nameExperiment, s1, propertyName))
    }

    private fun createPods(path: String, nameExperiment: String, experiment: String) {
        val file = File(path)
        file.writeText(Pod().transform(nameExperiment, experiment))
    }

}
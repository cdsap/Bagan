package com.cdsap.bagan.generator

import com.cdsap.bagan.generator.Versions.TEMP_FOLDER
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class BaganFileGenerator(
    private val sessionExperiment: String,
    private val bagan: Bagan,
    private val logger: Logger,
    private val commandExecutor: CommandExecutor
) {
    private val TAG = "BaganFileGenerator"


    fun generateExperiments(experiments: List<Experiment>) {
        logger.log(TAG, "Generating ${experiments.size} experiments")
        experiments.forEach {
            createExperiment(it)
        }
    }

    private fun createExperiment(experiment: Experiment) {
        val typeExperiments = if (bagan.experiments.incrementalChanges != null) {
            "incrementalChanges"
        } else {
            "combined"
        }
        val path = "$TEMP_FOLDER/${experiment.name}"

        logger.log(TAG, "Experiment ${experiment.name}")

        createFolder(
            path = path
        )

        val branch = getBranch(typeExperiments, experiment)
        createFileValues(
            path = "$path/values.yaml",
            bagan = bagan,
            nameExperiment = experiment.name,
            image = Versions.POD_INJECTOR,
            session = sessionExperiment,
            branch = branch
        )

        createChartFile(
            path = "$path/Chart.yaml",
            id = experiment.name
        )

        createTemplateFolder(
            path = path
        )

        createPods(
            path = "$path/templates/pod${experiment.name}.yaml",
            privateRepo = bagan.private,
            typeExperiments = typeExperiments
        )

        createConfigMaps(
            path = "$path/templates/configmap${experiment.name}.yaml",
            experiment = experiment,
            typeExperiments = typeExperiments,
            initialTask = bagan.gradleCommand,
            initialIterations = bagan.iterations
        )

        commandExecutor.execute("helm install ${experiment.name} -f $path/values.yaml $path/")
    }

    private fun getBranch(typeExperiments: String, experiment: Experiment): String {
        if (isIncremental(typeExperiments)) {
            return experiment.composeExperiment!!.branch
        } else {
            return if (experiment.branch.isEmpty()) "master" else experiment.branch
        }
    }

    private fun createChartFile(path: String, id: String) {
        logger.log(TAG, "creating Chart file $path")
        val file = File(path)
        file.writeText(
            Chart().transform(
                id,
                Versions.CHART_EXPERIMENT
            )
        )
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
        session: String,
        branch: String
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
                session = session,
                branch = branch
            )
        )
    }

    private fun createTemplateFolder(path: String) {
        logger.log(TAG, "creating template folder $path")
        Files.createDirectory(Paths.get("$path/templates"))
    }

    private fun createConfigMaps(
        path: String,
        experiment: Experiment,
        typeExperiments: String,
        initialTask: String,
        initialIterations: Int
    ) {
        logger.log(TAG, "creating configmap file $path")
        val file = File(path)

        var experiments = ""


        if (isIncremental(typeExperiments)) {
            if (experiment.composeExperiment != null) {
                val composed = experiment.composeExperiment
                experiments += "${ident(experiments)}extraLabel: '${composed}'\n"
                experiments += "${ident(experiments)}startingTask: '$initialTask'\n"
                experiments += "${ident(experiments)}startingTaskIterations: '$initialIterations'\n"
                experiments += "${ident(experiments)}taskExperimentation: '${composed?.taskExperiment}'\n"
                experiments += "${ident(experiments)}iterationsExperiments: '${composed?.iterationsExperiments}'\n"
                experiments += "${ident(experiments)}files: |\n"
                experiments += "${generateFiles(composed?.files)}\n"
            }

        } else {

            if (!experiment.properties.isEmpty()) {
                experiments += "${ident(experiments)}${ConfigMapExperiments.properties(properties = experiment.properties)}\n"
            }
            if (!experiment.branch.isEmpty()) {
                experiments += "${ident(experiments)}${ConfigMapExperiments.branch(branch = experiment.branch)}\n"
            }

            if (!experiment.gradleWrapperVersion.isEmpty()) {
                experiments += "${ident(experiments)}${ConfigMapExperiments.gradleWrapperVersion(version = experiment.gradleWrapperVersion)}"
            }
        }
        file.writeText(
            ConfigMap().transform(
                typeExperiments = typeExperiments,
                experiments = experiments
            )
        )
    }

    fun ident(experiments: String) = if (experiments.isBlank()) "" else "  "


    private fun createPods(
        path: String,
        privateRepo: Boolean,
        typeExperiments: String
    ) {
        logger.log(TAG, "creating pod file $path")

        val file = File(path)

        // initially only supporting compose mdode for private repos
        if (isIncremental(typeExperiments)) {
            file.writeText(PodSecureComposed().transform())
        } else {
            if (privateRepo) {
                file.writeText(PodSecure().transform())
            } else {
                file.writeText(Pod().transform())
            }
        }
    }


    private fun generateFiles(files: List<UnitExperiment>?): String {
        val ident = "         "
        var experiments = ""
        files?.forEach {
            experiments += "$ident${it.module.replace(":", "")}=${it.path}\n"
        }
        return experiments
    }

    fun isIncremental(typeExperiments : String) : Boolean = typeExperiments == "incrementalChanges"

}

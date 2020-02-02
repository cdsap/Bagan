package com.cdsap.bagan.experiments

import java.io.StringReader
import java.lang.Exception
import java.util.*


fun main() {
    ExecutionGenerator(".").init()
}

class ExecutionGenerator(val path: String) {

    fun init() {

        val coordinator = ChangesCoordinator()
        val scriptGenerator = ScriptGenerator("$path/execution.sh")
        val composedExperiments = ComposedExperiments(
            getUpdates(false),
            coordinator,
            scriptGenerator,
            getMetadata(false)
        )

        composedExperiments.execute()
    }

    fun getMetadata(isMocked: Boolean): MetadataI {
        if (isMocked) {
            val taskExperiment = "./gradlew  :Tinder:assembleInternal"
            val iterationsExperiment = "2"
            val startingTask = "./gradlew clean :Tinder:assembleInternal"
            val startingTaskIterations = "2"
            val extraLabel = "bootstraping"
            return MetadataI(
                startingTask,
                startingTaskIterations.toInt(),
                taskExperiment,
                iterationsExperiment.toInt(),
                extraLabel
            )
        } else {
            validate()
            val taskExperiment = System.getenv("taskExperimentation") as String
            val iterationsExperiment = System.getenv("iterationsExperiments") as String
            val startingTask = System.getenv("startingTask")
            val startingTaskIterations = System.getenv("startingTaskIterations")
            val extraLabel = System.getenv("extraLabel")
            return MetadataI(
                startingTask,
                startingTaskIterations.toInt(),
                taskExperiment,
                iterationsExperiment.toInt(),
                extraLabel
            )
        }
    }


    fun getUpdates(isMocked: Boolean): List<Update> {
        if (isMocked) {
            return listOf(
                Update(":Tinder", "Tinder/src/main/java/com/tinder/analytics/adapter/LeanplumFireworksEventAdapter.kt"),
                Update(":data", "data/src/main/java/com/tinder/data/mapper/MessageRequestBodyMapper.kt")
            )
        } else {
            val files = StringReader(System.getenv("files"))
            val newProps = Properties()

            newProps.load(files)

            return newProps.flatMap {
                println(it.key)
                println(it.value)
                listOf(Update(it.key as String, it.value as String))
            }

        }
    }

    private fun validate() {
        if (System.getenv("taskExperimentation") == null) {
            throw Exception("No taskExperiment value defined")
        }

        if (System.getenv("iterationsExperiments") == null) {
            throw Exception("No iterationsExperiment value defined")
        }

        if (System.getenv("files") == null) {
            throw Exception("No files defined to experiment")
        }

      //  if (System.getenv("extraLabel") == null) {
      //      throw Exception("No extra label defined to experiment")
      //  }

        if (System.getenv("startingTask") == null) {
            throw Exception("No starting Task defined to experiment")
        }

        if (System.getenv("startingTaskIterations") == null) {
            throw Exception("No starting task iterations defined to experiment")
        }
    }
}


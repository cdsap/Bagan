@file:Include("Bagan.kt")
@file:Include("Logger.kt")
@file:Include("ExperimentProvider.kt")
@file:Include("BaganConfFileProvider.kt")
@file:Include("BaganFileGenerator.kt")
@file:Include("DashboardProvider.kt")
@file:Include("CommandExecutor.kt")
@file:Include("MoshiProvider.kt")
@file:Include("Versions.kt")

package com.cdsap.bagan.experiments


import com.cdsap.bagan.experiments.Versions.CONF_FILE
import com.cdsap.bagan.experiments.Versions.PATH

import com.cdsap.bagan.experiments.Versions.TEMP_FOLDER
import java.io.File
import java.util.concurrent.ThreadLocalRandom

fun main(array: Array<String>) {

    val path = if (array != null && array.size > 0) {
        array[0]
    } else {
        PATH
    }

    val logger = LoggerImpl()
    val moshiProvider = MoshiProvider()
    val commandExecutor = CommandExecutor(logger, false)
    val experimentCoordinator = BaganGenerator(moshiProvider, commandExecutor, logger, path)
    experimentCoordinator.generate()
}

class BaganGenerator(
    private val moshiProvider: MoshiProvider,
    private val commandExecutor: CommandExecutor,
    private val logger: Logger,
    private val path: String
) {
    val TAG = "BaganGenerator"

    fun generate() {
        checkConfFile()
        checkTmpFolder()
        val baganConfFileProvider = BaganConfFileProviderImpl(moshiProvider)
        val dashBoardProvider = DashboardProvider(commandExecutor, path)
        val experimentProvider = ExperimentProvider(baganConfFileProvider)
        val sessionExperiment = registerExperiment()
        logger.log(TAG, "Bagan Experiment Session $sessionExperiment")

        val baganFileGenerator = BaganFileGenerator(sessionExperiment, logger, commandExecutor)
        var count = 0
        val experiments = experimentProvider.getExperiments().flatMap {
            count++
            listOf(Experiment("experiment$count".toLowerCase(), it))
        }

        logger.log(TAG, "Starting DashboardProvider")
        dashBoardProvider.generate(experiments, getCommands(baganConfFileProvider.getBaganConf().gradleCommand))

        experiments.forEach {
            baganFileGenerator.createExperiment(it.name, it.values, baganConfFileProvider.getBaganConf())
        }
    }

    private fun getCommands(command: String): List<String> {
        val values = command.split(" ")
        return values.filter {
            it != "./gradlew"
                    && it != "clean"
        }.toList()
    }

    private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    private fun registerExperiment(): String {
        return getStringExperiment()
    }


    private fun checkConfFile() {
        if (!File(CONF_FILE).exists()) {
            logger.log(TAG, "$CONF_FILE file not found.")
            throw Exception("Error: $CONF_FILE file not found.")
        }
    }

    private fun getStringExperiment() = (1..20)
        .map { ThreadLocalRandom.current().nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("")

    private fun checkTmpFolder() {
        val tmpFolder = File(TEMP_FOLDER)
        if (!tmpFolder.exists()) {
            tmpFolder.mkdir()
        }
    }
}

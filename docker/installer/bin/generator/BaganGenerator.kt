@file:Include("Bagan.kt")
@file:Include("Logger.kt")
@file:Include("ExperimentProvider.kt")
@file:Include("BaganConfFileProvider.kt")
@file:Include("BaganFileGenerator.kt")
@file:Include("DashboardProvider.kt")
@file:Include("CommandExecutor.kt")
@file:Include("MoshiProvider.kt")
@file:Include("Versions.kt")

package com.cdsap.bagan.generator


import com.cdsap.bagan.generator.Versions.CONF_FILE
import com.cdsap.bagan.generator.Versions.PATH

import com.cdsap.bagan.generator.Versions.TEMP_FOLDER
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
    val baganGenerator = BaganGenerator(moshiProvider, commandExecutor, logger, path)
    baganGenerator.generate()
}

class BaganGenerator(
    private val moshiProvider: MoshiProvider,
    private val commandExecutor: CommandExecutor,
    private val logger: Logger,
    private val path: String
) {
    private val TAG = "BaganGenerator"

    fun generate() {
        checkConfFile()
        checkTmpFolder()

        val bagan = BaganConfFileProviderImpl(moshiProvider, "${Versions.PATH}bagan_conf.json").getBaganConf()
        val experimentProvider = ExperimentProvider(bagan)
        val sessionExperiment = getSession()

        val dashBoardProvider = DashboardProvider(bagan, moshiProvider, path, logger, commandExecutor)
        val baganFileGenerator = BaganFileGenerator(
            sessionExperiment,
            bagan,
            logger,
            commandExecutor
        )
        val experiments = experimentProvider.getExperiments()

        dashBoardProvider.generate(experiments)
        baganFileGenerator.generateExperiments(experiments)
    }

    private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    private fun getSession(): String {
        val session = getSessionExperiment()
        logger.log(TAG, "Bagan Experiment Session $session")
        return session
    }

    private fun checkConfFile() {
        if (!File(CONF_FILE).exists()) {
            logger.log(TAG, "$CONF_FILE file not found.")
            throw Exception("Error: $CONF_FILE file not found.")
        }
    }

    private fun getSessionExperiment() = (1..20)
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

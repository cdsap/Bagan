package com.cdsap.bagan.experiments


import com.cdsap.bagan.dashboard.Dashboard
import com.cdsap.bagan.dashboard.DashboardProvider
import com.cdsap.bagan.experiments.Versions.CONF_FILE
import com.cdsap.bagan.experiments.Versions.TEMP_FOLDER
import java.io.File
import java.util.concurrent.ThreadLocalRandom

data class Experiment(val name: String, val values: String)

fun main() {
    val logger = LoggerImpl()
    val moshiProvider = MoshiProvider()
    val commandExecutor = CommandExecutor(logger, false)
    val experimentCoordinator = BaganGenerator(moshiProvider, commandExecutor, logger)
    experimentCoordinator.generate()
}

class BaganGenerator(
    val moshiProvider: MoshiProvider,
    private val commandExecutor: CommandExecutor,
    private val logger: Logger
) {

    fun generate() {
        checkFile()
        createTmpFolder()
        val baganConfFileProvider = BaganConfFileProviderImpl(moshiProvider)
        val dashBoardProvider = DashboardProvider(commandExecutor)

        val experimentProvider = ExperimentProvider(baganConfFileProvider)
        val sessionExperiment = registerExperiment()


        val baganFileGenerator = BaganFileGenerator(sessionExperiment, logger, commandExecutor)
        var count = 0
        val experiments = experimentProvider.getExperiments().flatMap {
            count++
            listOf(Experiment("experiment$count".toLowerCase(), it))
        }

        logger.log("Bagan Experiment Session $sessionExperiment")

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


    private fun checkFile() {
        if (!File(CONF_FILE).exists()) {
            throw Exception("Error: $CONF_FILE file not found.")
        }
    }

    private fun getStringExperiment() = (1..20)
        .map { _ -> ThreadLocalRandom.current().nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("")

    private fun createTmpFolder() {
        val tmpFolder = File(TEMP_FOLDER)
        if (tmpFolder.exists()) {
            tmpFolder.deleteRecursively()
        }

        tmpFolder.mkdir()

    }
}

@file:Include("MonitorReporting.kt")
@file:Include("GradleExperimentsProperties.kt")
@file:Include("Bagan.kt")
@file:Include("Logger.kt")
@file:Include("ExperimentProvider.kt")
@file:Include("BaganConfFileProvider.kt")
@file:Include("BaganFileGenerator.kt")
@file:Include("CommandExecutor.kt")
@file:Include("MoshiProvider.kt")
@file:Include("BaganJson.kt")
@file:Include("Property.kt")
@file:Include("Versions.kt")

package com.cdsap.bagan.experiments


import com.cdsap.bagan.experiments.Versions.CONF_FILE
import java.io.File
import java.util.concurrent.ThreadLocalRandom

data class Experiment(val name: String, val values: String)

fun main() {
    val logger = LoggerImpl()
    val moshiProvider = MoshiProvider()
    val commandExecutor = CommandExecutor(logger, true)
    val monitor = MonitorReporting(moshiProvider, commandExecutor)
    val experimentCoordinator = BaganGenerator(moshiProvider, commandExecutor, monitor, logger)
    experimentCoordinator.generate()
}

class BaganGenerator(
    val moshiProvider: MoshiProvider,
    private val commandExecutor: CommandExecutor,
    private val monitorReporting: MonitorReporting,
    private val logger: Logger
) {

    fun generate() {
        checkFile()
        val baganConfFileProvider = BaganConfFileProviderImpl(moshiProvider)
        val experimentProvider = ExperimentProvider(baganConfFileProvider)
        val sessionExperiment = registerExperiment()
        val baganFileGenerator = BaganFileGenerator(sessionExperiment, monitorReporting, logger)
        var count = 0
        val experiments = experimentProvider.getExperiments().flatMap {
            count++
            listOf(Experiment("experiment$count".toLowerCase(), it))
        }

        logger.log("Bagan Experiment Session $sessionExperiment")
        monitorReporting.insertExperiment(sessionExperiment)

        experiments.forEach {
            baganFileGenerator.createExperiment(it.name, it.values, baganConfFileProvider.getBaganConf())
            commandExecutor.execute("helm install -n ${it.name} -f ${it.name}/values.yaml ${it.name}/")
            count++
        }
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
}

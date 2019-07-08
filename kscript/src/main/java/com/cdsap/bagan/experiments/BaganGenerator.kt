package com.cdsap.bagan.experiments


import com.cdsap.bagan.experiments.Versions.CONF_FILE
import java.nio.file.Files
import java.nio.file.Paths
import com.cdsap.bagan.experiments.Versions.CURRENT_VERSION
import java.io.File
import java.util.concurrent.ThreadLocalRandom


fun main() {
    val moshiProvider = MoshiProvider()
    val commandExecutor = CommandExecutor()
    val monitor = MonitorReporting(moshiProvider, commandExecutor)
    val experimentCoordinator = ExperimentCoordinator(moshiProvider, commandExecutor, monitor)
    experimentCoordinator.generate()
}


class ExperimentCoordinator(
    val moshiProvider: MoshiProvider,
    private val commandExecutor: CommandExecutor,
    private val monitorReporting: MonitorReporting
) {

    fun generate() {
        checkFile()
        val experimentGenerator = ExperimentProvider(moshiProvider)
        val sessionExperiment = registerExperiment()
        val baganFileGenerator = BaganFileGenerator(sessionExperiment, monitorReporting)
        var count = 0

        monitorReporting.insertExperiment(sessionExperiment)

        experimentGenerator.getExperiments().forEach {
            val experiment = "experiment$count".toLowerCase()
            baganFileGenerator.createExperiment(experiment, it, experimentGenerator.bagan)
            commandExecutor.execute("helm install -n $experiment -f $experiment/values.yaml $experiment/")
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
        .map { i -> ThreadLocalRandom.current().nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("")
}

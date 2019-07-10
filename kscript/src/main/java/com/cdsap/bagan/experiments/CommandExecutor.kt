package com.cdsap.bagan.experiments

class CommandExecutor(
    private val logger: Logger,
    private val dryRun: Boolean
) {

    fun execute(command: String) {
        if (!dryRun) {
            Runtime.getRuntime().exec(command).waitFor()
            logger.log("executing $command")
        } else {
            logger.log("[dry-run]: executing $command")
        }

    }
}

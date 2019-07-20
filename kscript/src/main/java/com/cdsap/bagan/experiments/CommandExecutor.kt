package com.cdsap.bagan.experiments

import java.io.BufferedReader
import java.io.InputStreamReader


class CommandExecutor(
    private val logger: Logger,
    private val dryRun: Boolean
) {

    fun execute(command: String) {
        if (!dryRun) {
            logger.log("[CommandExecutor]: executing $command")
            try {
                val processBuilder = ProcessBuilder().command("bash", "-c", command)
                    .redirectErrorStream(true)
                    .start()
                val input = BufferedReader(InputStreamReader(processBuilder.inputStream))
                input.useLines {
                    it.forEach { logger.log("[CommandExecutor]:$it") }
                }

                input.close()
            } catch (err: Exception) {
                logger.log("[CommandExecutor]-[error]: ${err.message}")
            }
        } else {
            logger.log("[CommandExecutor]-[dry-run]: executing $command")
        }

    }
}

package com.cdsap.bagan.experiments

import java.io.BufferedReader
import java.io.InputStreamReader


class CommandExecutor(
    private val logger: Logger,
    private val dryRun: Boolean
) {
    val TAG = "CommandExecutor"

    fun execute(command: String) {
        if (!dryRun) {
            logger.log(TAG, "executing $command")
            try {
                val processBuilder = ProcessBuilder().command("bash", "-c", command)
                    .redirectErrorStream(true)
                    .start()
                val input = BufferedReader(InputStreamReader(processBuilder.inputStream))
                input.useLines { lines ->
                    lines.forEach { logger.log(TAG, it) }
                }

                input.close()
            } catch (err: Exception) {
                logger.log(TAG, "[error]: ${err.message}")
            }
        } else {
            logger.log(TAG, "[dry-run]: executing $command")
        }

    }
}

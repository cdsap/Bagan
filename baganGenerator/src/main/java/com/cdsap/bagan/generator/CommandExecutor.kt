package com.cdsap.bagan.generator

import java.io.BufferedReader
import java.io.InputStreamReader


class CommandExecutor(
    private val logger: Logger,
    private val dryRun: Boolean
) {
    private val TAG = "CommandExecutor"

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
                logger.log(TAG, "Error execution $command: ${err.message}")
            }
        } else {
            logger.log(TAG, "[dry-run]: executing $command")
        }

    }
}

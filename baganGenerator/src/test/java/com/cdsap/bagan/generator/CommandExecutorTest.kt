package com.cdsap.bagan.generator

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.kotest.core.spec.style.BehaviorSpec

class CommandExecutorTest : BehaviorSpec({
    given("CommandExecutorInstance") {
        `when`("we execute the command") {
            val logger = mock<Logger>()
            val dryrun = false
            val commandExecutor = CommandExecutor(logger, dryrun)
            then("chart template have been placed") {
                commandExecutor.execute("ls")
                verify(logger).log("CommandExecutor","executing ls")
            }
        }
        `when`("we apply dryrun") {
            val logger = mock<Logger>()
            val dryrun = true
            val commandExecutor = CommandExecutor(logger, dryrun)
            then("chart template have been placed") {
                commandExecutor.execute("ls")
                verify(logger).log("CommandExecutor","[dry-run]: executing ls")
            }
        }
    }
})


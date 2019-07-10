package com.cdsap.bagan.experiments

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.kotlintest.specs.BehaviorSpec

class CommandExecutorTest : BehaviorSpec({
    given("CommandExecutorInstance") {
        `when`("we execute the command") {
            val logger = mock<Logger>()
            val dryrun = false
            val commandExecutor = CommandExecutor(logger, dryrun)
            then("chart template have been placed") {
                commandExecutor.execute("ls")
                verify(logger).log("executing ls")
            }
        }
        `when`("we apply dryrun") {
            val logger = mock<Logger>()
            val dryrun = true
            val commandExecutor = CommandExecutor(logger, dryrun)
            then("chart template have been placed") {
                commandExecutor.execute("ls")
                verify(logger).log("[dry-run]: executing ls")
            }
        }
    }
})


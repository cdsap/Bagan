package com.cdsap.bagan.experiments

class CommandExecutor {

    fun execute(command: String) {
        Runtime.getRuntime().exec(command).waitFor()
    }
}

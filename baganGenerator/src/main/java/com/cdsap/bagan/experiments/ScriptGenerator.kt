package com.cdsap.bagan.experiments

import java.io.File

class ScriptGenerator(nameFile: String) {
    private val fileBash: File = File(nameFile)

    fun addCommand(command: String) {
        fileBash.appendText(command + "\n")
    }
}
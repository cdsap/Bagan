package com.cdsap.bagan.experiments

class LoggerImpl : Logger {
    override fun log(message: String) {
        println(message)
    }
}

interface Logger {
    fun log(message: String)
}
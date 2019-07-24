package com.cdsap.bagan.experiments

class LoggerImpl : Logger {
    override fun log(tag: String, message: String) {
        println("[$tag]: $message")
    }
}

interface Logger {
    fun log(tag: String, message: String)
}
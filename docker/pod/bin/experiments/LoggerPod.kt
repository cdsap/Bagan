package com.cdsap.bagan.experiments

class LoggerPodImpl : LoggerPod {
    override fun log(tag: String, message: String) {
        println("[$tag]: $message")
    }
}

interface LoggerPod {
    fun log(tag: String, message: String)
}
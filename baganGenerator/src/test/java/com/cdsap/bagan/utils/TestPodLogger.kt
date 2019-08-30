package com.cdsap.bagan.utils

import com.cdsap.bagan.experiments.LoggerPod

class TestPodLogger : LoggerPod {

    val log = mutableListOf<String>()
    override fun log(tag: String, message: String) {
        log.add(message)
    }

    fun containsLog(value: String): Boolean {
        return log.contains(value)
    }
}
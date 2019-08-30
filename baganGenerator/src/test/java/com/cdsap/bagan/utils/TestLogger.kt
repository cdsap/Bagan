package com.cdsap.bagan.utils

import com.cdsap.bagan.generator.Logger

class TestLogger : Logger {

    val log = mutableListOf<String>()
    override fun log(tag: String, message: String) {
        log.add(message)
    }

    fun containsLog(value: String): Boolean {
        return log.contains(value)
    }
}
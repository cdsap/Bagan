package com.cdsap.bagan.properties

import java.io.*
import java.util.*

fun main() {
    RewriteProperties().init()
}

class RewriteProperties {

    fun init() {
        println("[RewriteProperties]: Begin process")
        val b = StringReader(System.getenv("experiments"))
        val newProps = Properties()
        newProps.load(b)

        val inProperties = FileInputStream("gradle.properties")
        val props = Properties()
        props.load(inProperties)
        inProperties.close()

        val outProperties = FileOutputStream("gradle.properties")
        newProps.forEach {
            println("[RewriteProperties]: Writing property ${it.key}-${it.value}")
            props.setProperty(it.key.toString(), it.value.toString())
        }
        props.store(outProperties, null)
        outProperties.close()
        println("[RewriteProperties]: End process")

    }
}

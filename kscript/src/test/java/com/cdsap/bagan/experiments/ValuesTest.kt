package com.cdsap.bagan.experiments

import io.kotlintest.specs.BehaviorSpec

class ValuesTest : BehaviorSpec({
    given("Values file") {
        val repo = "http://git"
        val configmap = "configmap0"
        val name = "experiment0"
        val command = "./gradlew command"
        val image = "cdsap/bagan-pod-injector:0.1.4"
        val iterations = 10
        `when`("Parameters are defined") {
            val values = Values().transform(
                repository = repo,
                configMap = configmap,
                name = name,
                command = command,
                iterations = iterations,
                image = image
            )
            then("values template have been placed") {
                assert(
                    values == """
repository: $repo
configMaps : $configmap
name : $name
image: $image
command: $command
iterations: $iterations
""".trimIndent()
                )
            }

        }
    }
})


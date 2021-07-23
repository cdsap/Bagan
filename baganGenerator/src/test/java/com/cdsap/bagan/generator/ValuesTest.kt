package com.cdsap.bagan.generator

import com.cdsap.bagan.generator.Values
import io.kotest.core.spec.style.BehaviorSpec

class ValuesTest : BehaviorSpec({
    given("Values file") {
        val repo = "http://git"
        val name = "experiment0"
        val command = "./gradlew command"
        val image = "cdsap/bagan-pod-injector:0.1.5"
        val session = "xx1"
        val branch = "develop"
        val iterations = 10
        `when`("Parameters are defined") {
            val values = Values().transform(
                repository = repo,
                name = name,
                command = command,
                iterations = iterations,
                image = image,
                session = session,
                branch = branch
            )
            then("values template have been placed") {
                assert(
                    values == """
repository: $repo
branch: $branch
configMaps: configmap$name
pod: $name
session: $session
name: $name
image: $image
command: $command
iterations: $iterations
""".trimIndent()
                )
            }

        }
    }
})


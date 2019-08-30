package com.cdsap.bagan.generator

import com.cdsap.bagan.generator.Chart
import io.kotlintest.specs.BehaviorSpec

class ChartTest : BehaviorSpec({
    given("Chart file") {
        val id = "experiment0"
        val version = "0.1.0"
        `when`("Parameters are defined") {
            val values = Chart().transform(
                id = id,
                version = version
            )
            then("chart template have been placed") {
                assert(
                    values == """
apiVersion: v1
appVersion: "0.1.0"
description: Experiment resources like Pod and Configmap required in Bagan.
name: $id
version: $version"
    """.trimIndent()
                )
            }

        }
    }
})


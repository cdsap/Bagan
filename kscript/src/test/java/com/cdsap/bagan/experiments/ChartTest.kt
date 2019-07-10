package com.cdsap.bagan.experiments

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
appVersion: "1.0"
description: Chart, used for Bagan resource
name: $id
version: $version"
    """.trimIndent()
                )
            }

        }
    }
})


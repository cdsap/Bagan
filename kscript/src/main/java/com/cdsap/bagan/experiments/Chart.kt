package com.cdsap.bagan.experiments

class Chart {
    fun transform(
        id: String,
        version: String
    ) = """
apiVersion: v1
appVersion: "1.0"
description: Chart, used for Bagan resource
name: $id
version: $version"
    """.trimIndent()

}

package com.cdsap.bagan.experiments

class Values {
    fun transform(
        repository: String,
        configMap: String,
        name: String,
        command: String,
        iterations: Int
    ) = """
repository: $repository
configMaps : $configMap
name : $name
image: cdsap/bagan-pod-injector
command: $command
iterations: $iterations
""".trimIndent()


}
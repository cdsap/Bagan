package com.cdsap.bagan.experiments

import com.cdsap.bagan.experiments.Versions.URL_2

class MonitorReporting(
    private val moshiProvider: MoshiProvider,
    private val commandExecutor: CommandExecutor
) {

    fun insertPod(
        values: String,
        configMap: String,
        iterations: Int,
        pod: String,
        experiment: String
    ) {
        val po = PodRequest(
            values = values,
            iterations = iterations,
            configMap = configMap
        )

        val json = serializePodRequest(po)
        val url = "$URL_2/experiments/$experiment/$pod"
        val url2 = "curl -H Content-Type:application/json  -d $json  --request POST $url"
        commandExecutor.execute(url2)
    }

    fun insertExperiment(experiment: String) {
        val url = "$URL_2/experiments/$experiment"
        commandExecutor.execute("curl  -X POST $url")
    }

    fun serializePodRequest(request: PodRequest): String {
        val jsonAdapter = moshiProvider.adapter(PodRequest::class.java)
        return jsonAdapter.toJson(request)
    }

    data class PodRequest(
        val values: String,
        val configMap: String,
        val iterations: Int
    )
}


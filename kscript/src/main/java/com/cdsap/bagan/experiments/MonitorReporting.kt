package com.cdsap.bagan.experiments

import com.cdsap.bagan.experiments.Versions.URL_2

class MonitorReporting {

    fun insertExperiment(experiment: String) {
        val url = "$URL_2/experiments/$experiment"
        Runtime.getRuntime().exec("curl  -X POST $url").waitFor()
    }

    fun insertPod(experiment: String, pod: String, json: String) {

        val url = "$URL_2/experiments/$experiment/$pod"
        val url2 = "curl -H Content-Type:application/json  -d $json  --request POST $url"
        Runtime.getRuntime().exec(url2).waitFor()

    }
}


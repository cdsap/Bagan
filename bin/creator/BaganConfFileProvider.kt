package com.cdsap.bagan.experiments

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

interface BaganConfFileProvider {
    fun getBaganConf(): Bagan
}

class BaganConfFileProviderImpl(private val moshiProvider: MoshiProvider) : BaganConfFileProvider {
    override fun getBaganConf(): Bagan {
        val jsonAdapter = moshiProvider.adapter(BaganJson::class.java)

        val baganJson: BaganJson =
            jsonAdapter.fromJson(
                String(Files.readAllBytes(Paths.get("${Versions.PATH}bagan_conf.json")), StandardCharsets.US_ASCII)
            ) ?: throw Exception("Error parsing json file")
        return baganJson.bagan
    }
}

package com.cdsap.bagan.generator

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

interface BaganConfFileProvider {
    fun getBaganConf(): Bagan
}

class BaganConfFileProviderImpl(
    private val moshiProvider: MoshiProvider,
    private val filePath: String
) :
    BaganConfFileProvider {
    override fun getBaganConf(): Bagan {
        val jsonAdapter = moshiProvider.adapter(BaganJson::class.java)

        val baganJson: BaganJson =
            jsonAdapter.fromJson(
                String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.US_ASCII)
            ) ?: throw Exception("Error parsing json file")
        return baganJson.bagan
    }
}

package com.cdsap.bagan.generator

import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonEncodingException
import io.kotlintest.matchers.haveSubstring
import io.kotlintest.should
import io.kotlintest.shouldThrow
import io.kotlintest.specs.BehaviorSpec


class BaganConfFileProviderImplTest : BehaviorSpec({
    given("BaganConfFileProvider instance") {
        `when`("bagan config is correct") {
            val baganConfFileProvider =
                BaganConfFileProviderImpl(MoshiProvider(), "src/test/resources/bagan_conf_ok.json")
            val baganFile = baganConfFileProvider.getBaganConf()
            then("Bagan configuration object is properly parsed") {
                assert(baganFile.clusterName == "bagan")
                assert(baganFile.repository == "https://github.com/android/plaid.git")
                assert(baganFile.iterations == 20)
                assert(baganFile.experiments.properties!!.size == 2)

            }
        }
        `when`("Bagan config misses some required properties") {
            val baganConfFileProvider =
                BaganConfFileProviderImpl(MoshiProvider(), "src/test/resources/bagan_conf_incomplete.json")
            val exception = shouldThrow<JsonDataException> {
                baganConfFileProvider.getBaganConf()
            }
            then("error iterations showed") {
                exception.message should haveSubstring("Required value 'iterations'")
            }
        }
        `when`("Bagan config is incorrecct") {
            val baganConfFileProvider =
                BaganConfFileProviderImpl(MoshiProvider(), "src/test/resources/bagan_conf_incorrect.json")
            val exception = shouldThrow<JsonEncodingException> {
                baganConfFileProvider.getBaganConf()
            }
            then("error displayed") {
                exception.message should haveSubstring("Expected ':' at path \$.bagan.experiment")
            }
        }
        `when`("Bagan includes Talaiot options don't publish Task metrics") {
            val baganConfFileProvider =
                BaganConfFileProviderImpl(MoshiProvider(), "src/test/resources/bagan_conf_ok.json")
            val baganFile = baganConfFileProvider.getBaganConf()
            then("Bagan configuration object is properly parsed with Talaiot included in the conf") {
                assert(baganFile.talaiot?.publishTaskMetrics == false)
                assert(baganFile.talaiot?.publishBuildMetrics == true)
            }

        }
    }
})

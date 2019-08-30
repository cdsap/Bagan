package com.cdsap.bagan.generator

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class MoshiProvider {
    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    fun <T> adapter(c: Class<T>): JsonAdapter<T> {
        return moshi.adapter(c)
    }
}
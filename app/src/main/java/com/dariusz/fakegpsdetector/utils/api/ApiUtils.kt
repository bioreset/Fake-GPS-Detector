package com.dariusz.fakegpsdetector.utils.api

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

object ApiUtils {

    private var moshi: Moshi = Moshi.Builder().build()

    fun <T> prepareRequest(input: T, asClass: Class<T>): String {
        val adapter: JsonAdapter<T> = moshi.adapter(asClass)
        return adapter.toJson(input)
    }
}
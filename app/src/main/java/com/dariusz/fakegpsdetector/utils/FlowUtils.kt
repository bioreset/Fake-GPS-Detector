package com.dariusz.fakegpsdetector.utils

import androidx.lifecycle.asLiveData
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

object FlowUtils {

    inline fun <reified T> scrapeDataFromResponse(response: String?, asClass: Class<T>): Flow<T> =
        flow {
            val responseContent = response.toString()
            val moshi = Moshi.Builder().build()
            val adapter: JsonAdapter<T> = moshi.adapter(asClass)
            withContext(Dispatchers.IO) {
                emit(adapter.fromJson(responseContent)!!)
            }
        }

    @Suppress("UNCHECKED_CAST")
    @InternalCoroutinesApi
    suspend fun <T> collectTheFlow(flowData: Flow<T>): T {
        return flowData.let { it ->
            it.collect()
            it.flowOn(Dispatchers.IO)
            it.conflate()
        }.asLiveData().value ?: "" as T
    }
}

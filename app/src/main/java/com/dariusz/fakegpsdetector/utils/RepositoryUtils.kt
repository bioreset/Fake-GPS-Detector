package com.dariusz.fakegpsdetector.utils

import androidx.lifecycle.asLiveData
import com.dariusz.fakegpsdetector.utils.api.APICall.safeApiCall
import com.dariusz.fakegpsdetector.utils.api.APIResponseHandler.getResultFromAPI
import com.dariusz.fakegpsdetector.utils.cache.CacheCall.safeCacheCall
import com.dariusz.fakegpsdetector.utils.cache.CacheResponseHandler.getResultFromCache
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.*

object RepositoryUtils {

    fun <T> performApiCall(call: T): Flow<T> = flow {
        val safeCall = safeApiCall(IO) { call }
        val safeResult = getResultFromAPI(safeCall)!!
        emit(safeResult)
    }

    fun <T> performCacheCall(call: T?): Flow<T> = flow {
        val safeCall = safeCacheCall(IO) { call }
        val safeResult = getResultFromCache(safeCall)!!
        emit(safeResult)
    }

    fun <T> scrapeDataFromResponse(response: String?, asClass: Class<T>): Flow<T> = flow {
        val responseContent = response.toString()
        val moshi = Moshi.Builder().build()
        val adapter: JsonAdapter<T> = moshi.adapter(asClass)
        emit(adapter.fromJson(responseContent)!!)
    }

    @InternalCoroutinesApi
    suspend fun <T> collectTheFlow(flowData: Flow<T>, action: suspend (T) -> Any?): T? {
        return flowData.let { it ->
            it.collect { it2 ->
                action.invoke(it2)
            }
            it.flowOn(IO)
            it.conflate()
        }.asLiveData().value
    }
}

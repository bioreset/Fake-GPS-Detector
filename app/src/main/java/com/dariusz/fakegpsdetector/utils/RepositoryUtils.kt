package com.dariusz.fakegpsdetector.utils

import com.dariusz.fakegpsdetector.utils.api.APICall.safeApiCall
import com.dariusz.fakegpsdetector.utils.api.APIResponseHandler.getResultFromAPI
import com.dariusz.fakegpsdetector.utils.cache.CacheCall.safeCacheCall
import com.dariusz.fakegpsdetector.utils.cache.CacheResponseHandler.getResultFromCache
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object RepositoryUtils {

    fun <T> performApiCall(call: T?): Flow<T> = flow {
        val safeCall = safeApiCall(IO) { call }
        emit(getResultFromAPI(safeCall)!!)
    }

    fun <T> performCacheCall(call: T?): Flow<T> = flow {
        val safeCall = safeCacheCall(IO) { call }
        emit(getResultFromCache(safeCall)!!)
    }
}

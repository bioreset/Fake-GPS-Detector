package com.dariusz.fakegpsdetector.utils

import com.dariusz.fakegpsdetector.utils.api.APICall.safeApiCall
import com.dariusz.fakegpsdetector.utils.api.APIResponseHandler.getResultFromAPI
import com.dariusz.fakegpsdetector.utils.cache.CacheCall.safeCacheCall
import com.dariusz.fakegpsdetector.utils.cache.CacheResponseHandler.getResultFromCache
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

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
}

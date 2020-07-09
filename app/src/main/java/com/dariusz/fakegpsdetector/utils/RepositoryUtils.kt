package com.dariusz.fakegpsdetector.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.dariusz.fakegpsdetector.utils.api.APICall.safeApiCall
import com.dariusz.fakegpsdetector.utils.api.APIResponseHandler.getResultFromAPI
import com.dariusz.fakegpsdetector.utils.cache.CacheCall.safeCacheCall
import com.dariusz.fakegpsdetector.utils.cache.CacheResponseHandler.getResultFromCache
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.flow

object RepositoryUtils {

    fun <T> performApiCall(call: T?): LiveData<T> = flow {
        val safeCall = safeApiCall(IO) { call }
        emit(getResultFromAPI(safeCall)!!)
    }.asLiveData()

    fun <T> performCacheCall(call: T?): LiveData<T> = flow {
        val safeCall = safeCacheCall(IO) { call }
        emit(getResultFromCache(safeCall)!!)
    }.asLiveData()


}
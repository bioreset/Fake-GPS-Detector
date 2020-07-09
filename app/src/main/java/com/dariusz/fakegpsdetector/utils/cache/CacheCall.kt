package com.dariusz.fakegpsdetector.utils.cache

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

object CacheCall {

    suspend fun <T> safeCacheCall(
        dispatcher: CoroutineDispatcher,
        cacheCall: suspend () -> T?
    ): CacheStatus<T?> {
        return withContext(dispatcher) {
            try {
                CacheStatus.Success(cacheCall.invoke())
            } catch (throwable: Throwable) {
                CacheStatus.CacheError("generic-error: " + "Unknown Error")
            }
        }
    }
}


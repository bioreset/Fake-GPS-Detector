package com.dariusz.fakegpsdetector.utils.cache

object CacheResponseHandler {

    @Suppress("UNCHECKED_CAST")
    fun <T> getResultFromCache(response: CacheStatus<T>): T {
        return when (response) {

            is CacheStatus.CacheError -> {
                ("cache-error: " + "Unknown Cache Error") as T
            }

            is CacheStatus.Success -> {
                response.value ?: ("cache-error: " + "Data is empty") as T
            }
        }
    }

}
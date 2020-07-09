package com.dariusz.fakegpsdetector.utils.cache

sealed class CacheStatus<out T> {

    data class Success<out T>(val value: T) : CacheStatus<T>()

    data class CacheError(
        val errorHandle: String
    ) : CacheStatus<Nothing>()
}
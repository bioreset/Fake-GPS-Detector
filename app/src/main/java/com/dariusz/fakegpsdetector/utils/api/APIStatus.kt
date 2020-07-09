package com.dariusz.fakegpsdetector.utils.api

sealed class APIStatus<out T> {

    data class Success<out T>(val value: T) : APIStatus<T>()

    data class APIError(
        val error: String
    ) : APIStatus<Nothing>()

    data class NetworkError(
        val error: String
    ) : APIStatus<Nothing>()
}
package com.dariusz.fakegpsdetector.utils.api

object APIResponseHandler {

    @Suppress("UNCHECKED_CAST")
    fun <T> getResultFromAPI(response: APIStatus<T>): T {
        return when (response) {
            is APIStatus.APIError -> {
                ("generic: " +
                        "Unknown Error") as T
            }
            is APIStatus.NetworkError -> {
                ("network: " +
                        "Network Error") as T
            }
            is APIStatus.Success -> {
                response.value ?: ("api-error" + "Data is empty") as T
            }
        }
    }
}
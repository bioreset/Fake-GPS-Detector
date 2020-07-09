package com.dariusz.fakegpsdetector.utils.api

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.HttpException

object APICall {

    suspend fun <T> safeApiCall(
        dispatcher: CoroutineDispatcher,
        apiCall: suspend () -> T?
    ): APIStatus<T?> {
        return withContext(dispatcher) {
            try {
                APIStatus.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
                when (throwable) {
                    is IOException -> {
                        APIStatus.NetworkError(
                            "api-network-error: I/O EXCEPTION"
                        )
                    }
                    is HttpException -> {
                        val code = throwable.code()
                        val errorResponse =
                            convertErrorBody(
                                throwable
                            )
                        APIStatus.APIError(
                            "api-http-error: " +
                                    "Error code: $code; Error response: $errorResponse"
                        )
                    }
                    else -> {
                        APIStatus.APIError(
                            "api-error: " + "Unknown Error"
                        )
                    }
                }
            }
        }
    }

    private fun convertErrorBody(throwable: HttpException): String? {
        return try {
            throwable.response()?.errorBody()?.string()
        } catch (exception: Exception) {
            "Unknown error"
        }
    }


}
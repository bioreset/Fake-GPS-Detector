package com.dariusz.fakegpsdetector.api

import com.dariusz.fakegpsdetector.utils.Constants.API_HEADER
import com.dariusz.fakegpsdetector.utils.Constants.API_KEY
import com.dariusz.fakegpsdetector.utils.Constants.API_POST
import kotlinx.coroutines.InternalCoroutinesApi
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

@InternalCoroutinesApi
interface FakeGPSRestApi {

    @Headers(API_HEADER)
    @POST(API_POST)
    suspend fun checkLocation(
        @Body params: String,
        @Query("key") key: String = API_KEY
    ): String
}

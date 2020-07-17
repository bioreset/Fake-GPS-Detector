package com.dariusz.fakegpsdetector.api

import com.dariusz.fakegpsdetector.model.ApiResponseModel
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface FakeGPSRestApi {
    @Headers("Content-Type: application/json")
    @POST("geolocation/v1/geolocate")
    suspend fun checkLocation(@Body params: String, @Query("key") key: String): ApiResponseModel
}

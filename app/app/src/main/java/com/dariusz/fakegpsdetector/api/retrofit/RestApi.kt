package com.dariusz.fakegpsdetector.api.retrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface RestApi {
    @Headers("Content-Type: application/json")
    @POST("geolocation/v1/geolocate")
    fun checkLocation(@Body params: String, @Query("key") key: String): Call<String>

}
package com.dariusz.fakegpsdetector.api.retrofit

import com.dariusz.fakegpsdetector.R
import com.dariusz.fakegpsdetector.model.ApiResponseModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface RestApi {

    @Headers("Content-Type: application/json")
    @POST("geolocation/v1/geolocate?key=${R.string.google_api_key}")
    fun checkLocation(@Body jsonRequest: String) : Call<ApiResponseModel>

}
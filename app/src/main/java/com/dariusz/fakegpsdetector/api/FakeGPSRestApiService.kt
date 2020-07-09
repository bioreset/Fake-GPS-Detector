package com.dariusz.fakegpsdetector.api

class FakeGPSRestApiService {

    private val retrofit =
        ServiceBuilder.buildService(
            FakeGPSRestApi::class.java
        )

    suspend fun checkCurrentLocation(
        jsonBody: String,
        key: String = "AIzaSyBYvjzEoJUdnCCMkFBTEB_V4L-aQnWpBW8"
    ) =
        retrofit.checkLocation(jsonBody, key)

}
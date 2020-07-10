package com.dariusz.fakegpsdetector.api

class FakeGPSRestApiService {

    private val retrofit =
        ServiceBuilder.buildService(
            FakeGPSRestApi::class.java
        )

    suspend fun checkCurrentLocation(
        jsonBody: String,
        key: String = "AIzaSyCkZcvE_QrR-PCCpBP1g-LgrIOywhqSuAU"
    ) =
        retrofit.checkLocation(jsonBody, key)

}
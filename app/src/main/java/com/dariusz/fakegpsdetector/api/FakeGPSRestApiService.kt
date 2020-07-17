package com.dariusz.fakegpsdetector.api

interface FakeGPSRestApiService {

    suspend fun checkCurrentLocation(
        jsonBody: String
    ): String
}

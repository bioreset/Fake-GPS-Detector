package com.dariusz.fakegpsdetector.api

import com.dariusz.fakegpsdetector.model.ApiResponseModel

interface FakeGPSRestApiService {

    suspend fun checkCurrentLocation(
        jsonBody: String
    ): ApiResponseModel
}

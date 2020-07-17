package com.dariusz.fakegpsdetector.api

import com.dariusz.fakegpsdetector.di.NetworkModule.provideRetrofit
import com.dariusz.fakegpsdetector.model.ApiResponseModel

class FakeGPSRestApiServiceImpl : FakeGPSRestApiService {

    override suspend fun checkCurrentLocation(
        jsonBody: String
    ): ApiResponseModel =
        provideRetrofit().checkLocation(
            jsonBody,
            "AIzaSyCkZcvE_QrR-PCCpBP1g-LgrIOywhqSuAU"
        )
}

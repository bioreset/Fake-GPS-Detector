package com.dariusz.fakegpsdetector.api

import com.dariusz.fakegpsdetector.di.NetworkModule.provideFakeGPSRestApiService
import com.dariusz.fakegpsdetector.di.NetworkModule.provideRetrofit

class FakeGPSRestApiServiceImpl : FakeGPSRestApiService {

    override suspend fun checkCurrentLocation(
        jsonBody: String
    ): String =
        provideFakeGPSRestApiService(provideRetrofit()).checkLocation(jsonBody, "AIzaSyCkZcvE_QrR-PCCpBP1g-LgrIOywhqSuAU")

}
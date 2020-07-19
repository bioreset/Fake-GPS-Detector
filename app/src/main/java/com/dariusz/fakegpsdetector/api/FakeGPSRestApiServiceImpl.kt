package com.dariusz.fakegpsdetector.api

import com.dariusz.fakegpsdetector.di.NetworkModule.provideRetrofit
import kotlinx.coroutines.InternalCoroutinesApi

class FakeGPSRestApiServiceImpl : FakeGPSRestApiService {

    @InternalCoroutinesApi
    override suspend fun checkCurrentLocation(
        jsonBody: String
    ): String =
        provideRetrofit().checkLocation(
            jsonBody
        )
}

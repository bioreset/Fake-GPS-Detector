package com.dariusz.fakegpsdetector.api

import android.content.Context
import com.dariusz.fakegpsdetector.di.NetworkModule.provideRetrofit
import com.dariusz.fakegpsdetector.utils.ManageResponse.buildJSONRequest
import kotlinx.coroutines.InternalCoroutinesApi

class FakeGPSRestApiServiceImpl(private val context: Context) : FakeGPSRestApiService {

    @InternalCoroutinesApi
    override suspend fun checkCurrentLocation(
        jsonBody: String
    ): String =
        provideRetrofit().checkLocation(
            buildJSONRequest(context)
        )
}

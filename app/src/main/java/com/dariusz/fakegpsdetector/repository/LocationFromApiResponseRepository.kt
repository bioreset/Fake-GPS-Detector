package com.dariusz.fakegpsdetector.repository

import android.content.Context
import com.dariusz.fakegpsdetector.api.FakeGPSRestApiService
import com.dariusz.fakegpsdetector.db.dao.LocationFromApiResponseDao
import com.dariusz.fakegpsdetector.model.ApiResponseModel
import com.dariusz.fakegpsdetector.utils.CreateJSONRequest.buildJSONRequest
import com.dariusz.fakegpsdetector.utils.RepositoryUtils.performApiCall
import com.dariusz.fakegpsdetector.utils.RepositoryUtils.performCacheCall

class LocationFromApiResponseRepository
constructor(
    private val fakeGPSRestApiService: FakeGPSRestApiService,
    private val locationFromApiResponse: LocationFromApiResponseDao
) {

    suspend fun insert(apiResponse: ApiResponseModel) {
        deleteAll()
        performCacheCall(locationFromApiResponse.insert(apiResponse))
    }

    suspend fun selectAll(): ApiResponseModel =
        performCacheCall(locationFromApiResponse.getLocationFromApiInfo()).value
            ?: ApiResponseModel()

    private suspend fun deleteAll() =
        performCacheCall(locationFromApiResponse.deleteAllLocationFromApiRecords())

    private suspend fun checkCurrentLocationOfTheDevice(body: String) =
        performApiCall(fakeGPSRestApiService.checkCurrentLocation(body))

    suspend fun insertResponse(context: Context) =
        insert(checkCurrentLocationOfTheDevice(buildJSONRequest(context)).value!!)

    companion object {

        @Volatile
        private var instance: LocationFromApiResponseRepository? = null

        fun getInstance(
            fakeGPSRestApiService: FakeGPSRestApiService,
            locationFromApiResponse: LocationFromApiResponseDao
        ) =
            instance ?: synchronized(this) {
                instance ?: LocationFromApiResponseRepository(
                    fakeGPSRestApiService,
                    locationFromApiResponse
                ).also { instance = it }
            }
    }
}

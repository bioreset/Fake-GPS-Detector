package com.dariusz.fakegpsdetector.repository

import android.content.Context
import com.dariusz.fakegpsdetector.api.FakeGPSRestApiService
import com.dariusz.fakegpsdetector.db.dao.LocationFromApiResponseDao
import com.dariusz.fakegpsdetector.model.ApiResponseModel
import com.dariusz.fakegpsdetector.utils.ManageResponse.buildJSONRequest
import com.dariusz.fakegpsdetector.utils.ManageResponse.manageResponse
import com.dariusz.fakegpsdetector.utils.RepositoryUtils.collectTheFlow
import com.dariusz.fakegpsdetector.utils.RepositoryUtils.performApiCall
import com.dariusz.fakegpsdetector.utils.RepositoryUtils.performCacheCall
import com.dariusz.fakegpsdetector.utils.RepositoryUtils.scrapeDataFromResponse
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

@InternalCoroutinesApi
class LocationFromApiResponseRepository
@Inject
constructor(
    private val fakeGPSRestApiService: FakeGPSRestApiService,
    private val locationFromApiResponse: LocationFromApiResponseDao
) {

    suspend fun insert(apiResponse: ApiResponseModel) {
        deleteAll()
        performCacheCall(locationFromApiResponse.insert(apiResponse))
    }

    suspend fun selectAll() =
        performCacheCall(locationFromApiResponse.getLocationFromApiInfo())

    private suspend fun deleteAll() =
        performCacheCall(locationFromApiResponse.deleteAllLocationFromApiRecords())

    private suspend fun checkCurrentLocationOfTheDevice(body: String): ApiResponseModel? {
        val scrapedData = performApiCall(fakeGPSRestApiService.checkCurrentLocation(body))
        val apiResponseModel = ApiResponseModel::class.java
        return collectTheFlow(
            scrapeDataFromResponse(scrapedData.toString(), apiResponseModel)
        ) {
            return@collectTheFlow it
        }
    }
    
    suspend fun performAction(context: Context) =
        manageResponse(
            checkCurrentLocationOfTheDevice(buildJSONRequest(context))
        ) {
            insert(apiResponse = it)
        }

    suspend fun checkLocationStatus() =
        performCacheCall(locationFromApiResponse.getLocationFromApiInfo())
}

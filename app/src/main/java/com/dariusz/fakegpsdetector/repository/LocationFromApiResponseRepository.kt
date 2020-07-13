package com.dariusz.fakegpsdetector.repository

import android.content.Context
import androidx.lifecycle.asLiveData
import com.dariusz.fakegpsdetector.api.FakeGPSRestApiService
import com.dariusz.fakegpsdetector.db.dao.LocationFromApiResponseDao
import com.dariusz.fakegpsdetector.model.ApiResponseModel
import com.dariusz.fakegpsdetector.utils.CreateJSONRequest.buildJSONRequest
import com.dariusz.fakegpsdetector.utils.ManageResponse.manageResponse
import com.dariusz.fakegpsdetector.utils.RepositoryUtils.performApiCall
import com.dariusz.fakegpsdetector.utils.RepositoryUtils.performCacheCall
import javax.inject.Inject

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
        performCacheCall(locationFromApiResponse.getLocationFromApiInfo()).asLiveData().value
            ?: ApiResponseModel()

    private suspend fun deleteAll() =
        performCacheCall(locationFromApiResponse.deleteAllLocationFromApiRecords())

    private suspend fun checkCurrentLocationOfTheDevice(body: String) =
        performApiCall(fakeGPSRestApiService.checkCurrentLocation(body)).asLiveData().value

    suspend fun performAction(context: Context) =
        manageResponse(
            checkCurrentLocationOfTheDevice(buildJSONRequest(context))
        ) {
            insert(apiResponse = it)
        }

    suspend fun checkLocationStatus() =
        performCacheCall(locationFromApiResponse.getLocationFromApiInfo()).asLiveData().value
}

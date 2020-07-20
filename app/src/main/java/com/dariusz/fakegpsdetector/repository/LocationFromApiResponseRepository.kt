package com.dariusz.fakegpsdetector.repository

import com.dariusz.fakegpsdetector.api.FakeGPSRestApiService
import com.dariusz.fakegpsdetector.db.dao.LocationFromApiResponseDao
import com.dariusz.fakegpsdetector.model.ApiResponseModelJson
import com.dariusz.fakegpsdetector.model.CellTowerModel
import com.dariusz.fakegpsdetector.model.RoutersListModel
import com.dariusz.fakegpsdetector.utils.ManageResponse.asResponseToDb
import com.dariusz.fakegpsdetector.utils.ManageResponse.buildJSONRequest
import com.dariusz.fakegpsdetector.utils.ManageResponse.manageResponse
import com.dariusz.fakegpsdetector.utils.RepositoryUtils.performApiCall
import com.dariusz.fakegpsdetector.utils.RepositoryUtils.performCacheCall
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

@InternalCoroutinesApi
class LocationFromApiResponseRepository
@Inject
constructor(
    private val fakeGPSRestApiService: FakeGPSRestApiService,
    private val locationFromApiResponse: LocationFromApiResponseDao
) {

    private suspend fun insertAsFresh(apiResponse: ApiResponseModelJson) {
        performCacheCall(locationFromApiResponse.deleteAllLocationFromApiRecords())
        performCacheCall(locationFromApiResponse.insert(asResponseToDb(apiResponse)))
    }

    suspend fun selectAll() =
        performCacheCall(locationFromApiResponse.getLocationFromApiInfo())

    private suspend fun checkCurrentLocationOfTheDevice(body: String): String? =
        performApiCall(fakeGPSRestApiService.checkCurrentLocation(body))

    suspend fun manageResponseAfterAction(
        cellData: List<CellTowerModel>?,
        routersData: List<RoutersListModel>?
    ): Unit? {
        val check = checkCurrentLocationOfTheDevice(buildJSONRequest(cellData, routersData))
        return manageResponse(check!!) { insertAsFresh(it) }
    }

    suspend fun checkLocationStatus() =
        performCacheCall(locationFromApiResponse.getLocationFromApiInfo())
}

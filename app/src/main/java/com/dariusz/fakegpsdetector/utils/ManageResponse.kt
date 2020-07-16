package com.dariusz.fakegpsdetector.utils

import android.content.Context
import androidx.lifecycle.asLiveData
import com.dariusz.fakegpsdetector.model.ApiRequestModel
import com.dariusz.fakegpsdetector.model.ApiResponseModel
import com.dariusz.fakegpsdetector.utils.Injectors.getCellTowersRepository
import com.dariusz.fakegpsdetector.utils.Injectors.getRoutersListRepository
import com.dariusz.fakegpsdetector.utils.RepositoryUtils.collectTheFlow
import com.dariusz.fakegpsdetector.utils.RepositoryUtils.performCacheCall
import com.dariusz.fakegpsdetector.utils.RepositoryUtils.scrapeDataFromResponse
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.InternalCoroutinesApi
import org.json.JSONObject

@InternalCoroutinesApi
object ManageResponse {

    private val moshi: Moshi = Moshi.Builder().build()

    suspend fun manageResponse(response: ApiResponseModel?, cacheCall: suspend (ApiResponseModel) -> Unit) =
        performCacheCall(parseJSON(response.toString())?.let { cacheCall.invoke(it) })

    suspend fun buildJSONRequest(context: Context): String {
        val cellData = getCellTowersRepository(context).selectAll().asLiveData().value
        val routersData = getRoutersListRepository(context).selectAll().asLiveData().value
        val adapter: JsonAdapter<ApiRequestModel> = moshi.adapter(ApiRequestModel::class.java)
        return if (cellData != null && routersData != null) {
            adapter.toJson(
                ApiRequestModel(
                    cellTowersList = cellData,
                    routersList = routersData
                )
            )
        } else {
            ""
        }
    }

    private suspend fun parseJSON(response: String) =
        collectTheFlow(scrapeDataFromResponse(response, ApiResponseModel::class.java)) {
            return@collectTheFlow it
        }

    private fun parseJSONResponse(response: String): ApiResponseModel {
        return if (response.contains("location")) {
            val json = JSONObject(response)
            val jsonlocation = json.getJSONObject("location")
            val lat = jsonlocation.getDouble("lat")
            val lng = jsonlocation.getDouble("lng")
            val accuracy = json.getInt("accuracy")
            ApiResponseModel(
                status = "location",
                lng = lng,
                lat = lat,
                accuracy = accuracy
            )
        } else {
            ApiResponseModel(
                status = "location_error",
                lng = 0.0,
                lat = 0.00,
                accuracy = 0
            )
        }
    }
}

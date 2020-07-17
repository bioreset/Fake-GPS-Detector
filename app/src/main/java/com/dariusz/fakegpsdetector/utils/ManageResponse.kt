package com.dariusz.fakegpsdetector.utils

import android.content.Context
import androidx.lifecycle.asLiveData
import com.dariusz.fakegpsdetector.model.ApiRequestModel
import com.dariusz.fakegpsdetector.model.ApiResponseModel
import com.dariusz.fakegpsdetector.utils.FlowUtils.collectTheFlow
import com.dariusz.fakegpsdetector.utils.FlowUtils.scrapeDataFromResponse
import com.dariusz.fakegpsdetector.utils.Injectors.getCellTowersRepository
import com.dariusz.fakegpsdetector.utils.Injectors.getRoutersListRepository
import com.dariusz.fakegpsdetector.utils.RepositoryUtils.performCacheCall
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
object ManageResponse {

    private val moshi: Moshi = Moshi.Builder().build()

    suspend fun manageResponse(
        response: ApiResponseModel?,
        cacheCall: suspend (ApiResponseModel) -> Unit
    ) =
        performCacheCall(parseJSON(response.toString()).let { cacheCall.invoke(it) })

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
        collectTheFlow(scrapeDataFromResponse(response, ApiResponseModel::class.java))
}

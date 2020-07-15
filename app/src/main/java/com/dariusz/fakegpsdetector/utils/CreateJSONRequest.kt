package com.dariusz.fakegpsdetector.utils

import android.content.Context
import com.dariusz.fakegpsdetector.model.ApiRequestModel
import com.dariusz.fakegpsdetector.utils.Injectors.getCellTowersRepository
import com.dariusz.fakegpsdetector.utils.Injectors.getRoutersListRepository
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

object CreateJSONRequest {

    suspend fun buildJSONRequest(context: Context): String {
        val cellData = getCellTowersRepository(context).selectAll()
        val routersData = getRoutersListRepository(context).selectAll()
        val moshi: Moshi = Moshi.Builder().build()
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
}

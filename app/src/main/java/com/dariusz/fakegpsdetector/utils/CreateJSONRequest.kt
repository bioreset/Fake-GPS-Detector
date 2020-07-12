package com.dariusz.fakegpsdetector.utils

import android.content.Context
import com.dariusz.fakegpsdetector.model.ApiRequestModel
import com.dariusz.fakegpsdetector.utils.Injectors.getCellTowersRepository
import com.dariusz.fakegpsdetector.utils.Injectors.getRoutersListRepository
import com.google.gson.GsonBuilder

object CreateJSONRequest {

    suspend fun buildJSONRequest(context: Context): String {
        val cellData = getCellTowersRepository(context).selectAll()
        val routersData = getRoutersListRepository(context).selectAll()
        val gsonPretty = GsonBuilder().setPrettyPrinting().create()
        return if (cellData != null && routersData != null) {
            gsonPretty.toJson(
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
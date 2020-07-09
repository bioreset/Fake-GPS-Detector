package com.dariusz.fakegpsdetector.utils

import android.content.Context
import com.dariusz.fakegpsdetector.model.ApiRequestModel
import com.dariusz.fakegpsdetector.utils.Injectors.getCellTowersRepository
import com.dariusz.fakegpsdetector.utils.Injectors.getRoutersListRepository
import com.google.gson.GsonBuilder

object CreateJSONRequest {

    private lateinit var request: ApiRequestModel

    suspend fun buildJSONRequest(context: Context): String {

        val cellData = getCellTowersRepository(context).selectAll()

        val routersData = getRoutersListRepository(context).selectAll()

        val gsonPretty = GsonBuilder().setPrettyPrinting().create()

        if (cellData != null && routersData != null) {
            request = ApiRequestModel(
                cellTowersList = cellData,
                routersList = routersData
            )
        }

        return gsonPretty.toJson(request)

    }


}
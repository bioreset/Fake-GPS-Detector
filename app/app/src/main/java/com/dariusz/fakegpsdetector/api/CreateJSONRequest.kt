package com.dariusz.fakegpsdetector.api

import android.content.Context
import android.util.Log
import com.dariusz.fakegpsdetector.api.apimodel.ApiRequestModel
import com.dariusz.fakegpsdetector.repository.CellTowersRepository
import com.dariusz.fakegpsdetector.repository.RoutersListRepository
import com.google.gson.GsonBuilder

class CreateJSONRequest(context: Context) {

    private var cellData = CellTowersRepository.getInstance(context).selectAll()

    private var routersData = RoutersListRepository.getInstance(context).selectAll()

    private var gsonPretty = GsonBuilder().setPrettyPrinting().create()

    private lateinit var request : ApiRequestModel

    fun buildJSONRequest(): String {

        request = ApiRequestModel(cellTowersList = cellData, routersList = routersData)

        return gsonPretty.toJson(request)

    }


}
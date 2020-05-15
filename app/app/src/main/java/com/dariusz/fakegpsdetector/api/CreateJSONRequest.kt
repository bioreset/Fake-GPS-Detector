package com.dariusz.fakegpsdetector.api

import android.content.Context
import com.dariusz.fakegpsdetector.api.apimodel.ApiRequestModel
import com.dariusz.fakegpsdetector.repository.CellTowersRepository
import com.dariusz.fakegpsdetector.repository.RoutersListRepository
import com.google.gson.GsonBuilder

class CreateJSONRequest(private var context: Context) {

    private var cellData = CellTowersRepository.getInstance(context).selectAll()

    private var routersData = RoutersListRepository.getInstance(context).selectAll()

    private var gsonPretty = GsonBuilder().setPrettyPrinting().create()

    fun buildJSONRequest(): String {

        /*
        var routerModel1 = RoutersListModel(macAddress = "18:35:d1:f6:63:3f", level = -43, ssid = null)
        var routerModel2 = RoutersListModel(macAddress = "18:35:d1:c4:e2:ef", level = -46, ssid = null)

        var tower1 = CellTowerModel(cellId = 128142964, mobileCountryCode = "234", mobileNetworkCode = "10", locationAreaCode = 192, signalStrength = -96)
        var tower2 = CellTowerModel(cellId = 402945, mobileCountryCode = "234", mobileNetworkCode = "20", locationAreaCode = 2085, signalStrength =  null)


        var routersList =  arrayListOf(routerModel1, routerModel2)
        var towersList =  arrayListOf(tower1, tower2)

        */

        var request = ApiRequestModel(cellTowersList = cellData, routersList = routersData)

        return gsonPretty.toJson(request)

    }


}
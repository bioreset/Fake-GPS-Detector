package com.dariusz.fakegpsdetector.api.apimodel

import com.dariusz.fakegpsdetector.model.CellTowerModel
import com.dariusz.fakegpsdetector.model.RoutersListModel
import com.google.gson.annotations.SerializedName

data class ApiRequestModel(
        @SerializedName("wifiAccessPoints")
        val routersList: List<RoutersListModel>,
        @SerializedName("cellTowers")
        val cellTowersList: List<CellTowerModel>

)
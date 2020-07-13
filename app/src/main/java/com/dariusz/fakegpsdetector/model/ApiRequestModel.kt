package com.dariusz.fakegpsdetector.model

import com.google.gson.annotations.SerializedName

data class ApiRequestModel(
    @SerializedName("wifiAccessPoints")
    val routersList: List<RoutersListModel>,
    @SerializedName("cellTowers")
    val cellTowersList: List<CellTowerModel>

)

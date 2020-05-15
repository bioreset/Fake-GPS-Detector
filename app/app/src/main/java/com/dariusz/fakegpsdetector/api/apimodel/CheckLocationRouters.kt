package com.dariusz.fakegpsdetector.api.apimodel

import com.dariusz.fakegpsdetector.model.RoutersListModel
import com.google.gson.annotations.SerializedName

data class CheckLocationRouters(
        @SerializedName("wifiAccessPoints")
        val routersList: List<RoutersListModel>
)

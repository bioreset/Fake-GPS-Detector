package com.dariusz.fakegpsdetector.api.apimodel

import com.dariusz.fakegpsdetector.model.CellTowerModel
import com.google.gson.annotations.SerializedName

data class CheckLocationCellTowers(
        @SerializedName("cellTowers")
        val cellTowersList: List<CellTowerModel>
)
package com.dariusz.fakegpsdetector.model

import android.os.Build
import android.telephony.CellInfoGsm
import androidx.annotation.RequiresApi

data class CellTowerModel(
    val cellId: Int?,
    val locationAreaCode: Int?,
    val mobileCountryCode: String?,
    val mobileNetworkCode: String?,
    val signalStrength: Int?
){
    companion object {
        @RequiresApi(Build.VERSION_CODES.P)
        fun newCellTowersList(cltwm:List<CellInfoGsm>): List<CellTowerModel> {
            return cltwm.map { CellTowerModel(
                cellId = it.cellIdentity.cid,
                locationAreaCode = it.cellIdentity.lac,
                mobileCountryCode = it.cellIdentity.mccString,
                mobileNetworkCode = it.cellIdentity.mncString,
                signalStrength = it.cellSignalStrength.dbm
            )}
        }
    }
}



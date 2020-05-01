package com.dariusz.fakegpsdetector.model

import android.os.Build
import android.telephony.CellInfoLte
import androidx.annotation.RequiresApi

data class CellTowerModel(
    val cellId: Int,
    val locationAreaCode: Int,
    val mobileCountryCode: String?,
    val mobileNetworkCode: String?,
    val signalStrength: Int
) {
    companion object {
        @RequiresApi(Build.VERSION_CODES.P)
         fun newCellTowersList(cltwm: List<CellInfoLte>): List<CellTowerModel> {
            return cltwm.map {
                CellTowerModel(
                    cellId = it.cellIdentity.ci,
                    locationAreaCode = it.cellIdentity.tac,
                    mobileCountryCode = it.cellIdentity.mccString,
                    mobileNetworkCode = it.cellIdentity.mncString,
                    signalStrength = it.cellSignalStrength.dbm
                )
            }
        }

    }
}

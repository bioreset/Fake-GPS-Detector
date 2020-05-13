package com.dariusz.fakegpsdetector.model

import android.os.Build
import android.telephony.CellInfoLte
import androidx.annotation.RequiresApi
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "celltowers_table")
data class CellTowerModel(
        @ColumnInfo(name = "cellId") val cellId: Int,
        @ColumnInfo(name = "locationAreaCode") val locationAreaCode: Int,
        @ColumnInfo(name = "mobileCountryCode") val mobileCountryCode: String?,
        @ColumnInfo(name = "mobileNetworkCode") val mobileNetworkCode: String?,
        @ColumnInfo(name = "signalStrength") val signalStrength: Int
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

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

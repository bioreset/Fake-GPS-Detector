package com.dariusz.fakegpsdetector.model

import android.os.Build
import android.telephony.CellInfoGsm
import android.telephony.CellInfoLte
import android.telephony.CellInfoWcdma
import androidx.annotation.RequiresApi
import androidx.room.*
import com.google.gson.annotations.SerializedName

@Entity(tableName = "celltowers_table")
data class CellTowerModel(
        @ColumnInfo(name = "cellId")
        @SerializedName("cellId")
        val cellId: Int,
        @ColumnInfo(name = "locationAreaCode")
        @SerializedName("locationAreaCode")
        val locationAreaCode: Int,
        @ColumnInfo(name = "mobileCountryCode")
        @SerializedName("mobileCountryCode")
        val mobileCountryCode: String?,
        @ColumnInfo(name = "mobileNetworkCode")
        @SerializedName("mobileNetworkCode")
        val mobileNetworkCode: String?,
        @ColumnInfo(name = "signalStrength")
        val signalStrength: Int?,
        @ColumnInfo(name = "type")
        val type: String?
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

    companion object {

        @RequiresApi(Build.VERSION_CODES.P)
        fun newCellTowersList(cltwm: List<CellInfoLte>?): List<CellTowerModel>? {
            return cltwm?.map {
                CellTowerModel(
                        cellId = it.cellIdentity.ci,
                        locationAreaCode = it.cellIdentity.tac,
                        mobileCountryCode = it.cellIdentity.mccString,
                        mobileNetworkCode = it.cellIdentity.mncString,
                        signalStrength = it.cellSignalStrength.dbm,
                        type = "LTE"
                )
            }
        }


    }
}

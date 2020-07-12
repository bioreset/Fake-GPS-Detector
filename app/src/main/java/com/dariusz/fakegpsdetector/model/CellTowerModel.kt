package com.dariusz.fakegpsdetector.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
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
    val signalStrength: Int?
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

}

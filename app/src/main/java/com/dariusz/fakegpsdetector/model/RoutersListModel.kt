package com.dariusz.fakegpsdetector.model

import android.net.wifi.ScanResult
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "routers_table")
data class RoutersListModel(
        @ColumnInfo(name = "ssid") val ssid: String?,
        @ColumnInfo(name = "macAddress") val macAddress: String? = null,
        @ColumnInfo(name = "frequency") val frequency: Int? = null,
        @ColumnInfo(name = "level") val level: Int? = null
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

    companion object {
        fun newRoutersList(rmdl: List<ScanResult>): List<RoutersListModel> {
            return rmdl.map {
                RoutersListModel(
                        ssid = it.SSID,
                        macAddress = it.BSSID,
                        frequency = it.frequency,
                        level = it.level
                )
            }
        }
    }
}
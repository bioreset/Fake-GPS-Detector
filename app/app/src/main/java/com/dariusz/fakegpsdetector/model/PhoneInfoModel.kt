package com.dariusz.fakegpsdetector.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "phoneinfo_table")
data class PhoneInfoModel(
        @ColumnInfo(name = "homeMobileCountryCode")
        val homeMobileCountryCode: String?,
        @ColumnInfo(name = "homeMobileNetworkCode")
        val homeMobileNetworkCode: String?,
        @ColumnInfo(name = "radioType")
        val radioType: String?,
        @ColumnInfo(name = "carrier")
        val carrier: String?

) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

}
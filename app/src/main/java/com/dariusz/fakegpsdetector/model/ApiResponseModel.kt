package com.dariusz.fakegpsdetector.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "locationfromapix_table")
data class ApiResponseModel(
    @ColumnInfo(name = "status")
    var status: String? = "",
    @ColumnInfo(name = "lat")
    @SerializedName("lat")
    var lat: Double? = 0.0,
    @SerializedName("lng")
    @ColumnInfo(name = "lng")
    var lng: Double? = 0.0,
    @ColumnInfo(name = "accuracy")
    @SerializedName("accuracy")
    var accuracy: Int? = 0
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0
}
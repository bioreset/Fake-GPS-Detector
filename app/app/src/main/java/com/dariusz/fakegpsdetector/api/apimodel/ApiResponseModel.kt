package com.dariusz.fakegpsdetector.api.apimodel

import com.google.gson.annotations.SerializedName

data class ApiResponseModel(
        val status: String,
        @SerializedName("long")
        val long: Double?,
        @SerializedName("lat")
        val lat: Double?,
        @SerializedName("accuracy")
        val accuracy: Int?
)
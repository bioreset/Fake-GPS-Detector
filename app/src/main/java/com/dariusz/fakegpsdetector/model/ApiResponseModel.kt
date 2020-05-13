package com.dariusz.fakegpsdetector.model

data class ApiResponseModel (
        var status: String,
        val long: Double?,
        val lat: Double?
)
package com.dariusz.fakegpsdetector.api

import android.content.Context
import com.google.gson.GsonBuilder

class CreateJSONRequestTEST(private var context: Context) {

    private var gsonPretty = GsonBuilder().setPrettyPrinting().create()

    private var startingPoint: String = ""

    private var cellDataStuff: String = ""

    private var closingCellData: String = ""

    private var routersStuff: String = ""

    private var closingRequest: String = ""

    fun createJSONRequest(): String {
        return buildPrettyRequest()
    }

    private fun buildPrettyRequest(): String {
        startingPoint = """{"homeMobileCountryCode": 123, "homeMobileNetworkCode": 456, "radioType": "lte", "carrier": "giffgaff","considerIp": "true","cellTowers": [ """

        cellDataStuff = """{"cellId": 123, "locationAreaCode": 456, "mobileCountryCode": 789, "mobileNetworkCode": 147}"""

        closingCellData = """ ], "wifiAccessPoints": ["""

        routersStuff = """{"macAddress": "xDxDxD", "signalStrength": 666, "signalToNoiseRatio": 0 }"""

        closingRequest = """] }"""

        return gsonPretty.toJson(startingPoint + cellDataStuff + closingCellData + routersStuff + closingRequest)
    }


}
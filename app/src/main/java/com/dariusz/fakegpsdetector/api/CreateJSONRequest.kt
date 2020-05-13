package com.dariusz.fakegpsdetector.api

import android.content.Context
import android.util.Log
import com.dariusz.fakegpsdetector.repository.CellTowersRepository
import com.dariusz.fakegpsdetector.repository.PhoneInfoRepository
import com.dariusz.fakegpsdetector.repository.RoutersListRepository
import com.google.gson.GsonBuilder

class CreateJSONRequest(context: Context) {

    private var cellData = CellTowersRepository.getInstance(context).selectAll()

    private var phoneInfo = PhoneInfoRepository.getInstance(context).selectAll()

    private var routersData = RoutersListRepository.getInstance(context).selectAll()

    private var gsonPretty = GsonBuilder().setPrettyPrinting().create()

    private var startingPoint : String = ""

    private var cellDataStuff : String = ""

    private var closingCellData : String = ""

    private var routersStuff : String = ""

    private var closingRequest : String = ""

    fun createJSONRequest(){
        Log.i("XD", buildRequest())
    }

    private fun buildRequest(): String {
        for (phoneInfoModel in phoneInfo) {
            startingPoint = """{ \n
                                      "homeMobileCountryCode": ${phoneInfoModel.homeMobileCountryCode},\n
                                      "homeMobileNetworkCode": ${phoneInfoModel.homeMobileNetworkCode},\n
                                      "radioType": "${phoneInfoModel.radioType}",\n
                                      "carrier": "${phoneInfoModel.carrier}",\n
                                      "considerIp": "true",\n
                                      "cellTowers": [\n
                                      
                                      """

        }
        for (cellDatum in cellData) {
             cellDataStuff = """
                                            {\n
                                              "cellId": ${cellDatum.cellId},\n
                                              "locationAreaCode": ${cellDatum.locationAreaCode},\n
                                              "mobileCountryCode": ${cellDatum.mobileCountryCode},\n
                                              "mobileNetworkCode": ${cellDatum.mobileNetworkCode}\n
                                            }\n
                                            """
        }

             closingCellData = """
            
                                                ],\n
                                                  "wifiAccessPoints": [\n
                                                  
                                                """


        for (routersDatum in routersData) {
             routersStuff = """
                                                    {\n
                                                    "macAddress": "${routersDatum.macAddress}",\n
                                                    "signalStrength": ${routersDatum.level},\n
                                                    "signalToNoiseRatio": 0\n
                                                    }\n
                                                    
                                                    """
        }

         closingRequest = """
                                    ]\n
                                }\n
                                """

        return gsonPretty.toJson(startingPoint + cellDataStuff + closingCellData + routersStuff + closingRequest)
    }




}
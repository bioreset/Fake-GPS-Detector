package com.dariusz.fakegpsdetector.api

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.dariusz.fakegpsdetector.api.apimodel.ApiResponseModel
import com.dariusz.fakegpsdetector.api.retrofit.RestApiService

class ApiTestService : Service() {

    private var api: RestApiService = RestApiService()

    private lateinit var apimodel: ApiResponseModel

    private var test: String = """{
                                   "considerIp":"true",
                                   "wifiAccessPoints":[
                                      {
                                         "macAddress":"18:35:d1:f6:63:3f",
                                         "signalStrength":-43,
                                         "signalToNoiseRatio":0
                                      },
                                      {
                                         "macAddress":"18:35:d1:c4:e2:ef",
                                         "signalStrength":-46,
                                         "signalToNoiseRatio":0
                                      }
                                   ]
                                }"""

    override fun onCreate() {
        api.checkLocation(test)
        //     apimodel = api.result
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("XDXD", apimodel.toString())
        return super.onStartCommand(intent, flags, startId)
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


}
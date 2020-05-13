package com.dariusz.fakegpsdetector.phoneinfo

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.telephony.TelephonyManager
import com.dariusz.fakegpsdetector.model.PhoneInfoModel
import com.dariusz.fakegpsdetector.repository.PhoneInfoRepository

class PhoneInfoService : Service() {

    private val telephonyManager: TelephonyManager
        get() = applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    @SuppressLint("MissingPermission")
    private var radiotype = telephonyManager.dataNetworkType.toString()

    private var mccmnc = telephonyManager.networkOperator

    private var carriername = telephonyManager.networkOperatorName

    private lateinit var result: List<String>

    override fun onCreate() {
        super.onCreate()
        prepareData()
        newInfoList()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        addToDb()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun prepareData(): List<String> {
        result = mutableListOf(radiotype, mccmnc, carriername)
        return result
    }

    private fun newInfoList(): PhoneInfoModel {
        return PhoneInfoModel(
                homeMobileCountryCode = prepareData()[1].substring(0, 2),
                homeMobileNetworkCode = prepareData()[1].substring(3, 5),
                radioType = prepareData()[0],
                carrier = prepareData()[2]
        )
    }

    private fun addToDb() {
           return PhoneInfoRepository.getInstance(this.applicationContext).insert(newInfoList())
    }

}


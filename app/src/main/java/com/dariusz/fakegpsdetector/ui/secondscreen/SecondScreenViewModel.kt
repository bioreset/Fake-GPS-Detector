package com.dariusz.fakegpsdetector.ui.secondscreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.dariusz.fakegpsdetector.wifi.WifiScanLiveData

class SecondScreenViewModel(application: Application) : AndroidViewModel(application) {

    private val wifiData = WifiScanLiveData(application)

    fun getWifiData() = wifiData
}
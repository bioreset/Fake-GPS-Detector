package com.dariusz.fakegpsdetector.ui

import android.Manifest
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.dariusz.fakegpsdetector.gps.GpsStatusLiveData
import com.dariusz.fakegpsdetector.permissions.PermissionCheckLiveData
import com.dariusz.fakegpsdetector.wifistatus.WifiStatusLiveData

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val gpsStatus = GpsStatusLiveData(application)

    fun getGpsStatus() = gpsStatus

    private val permissionCheck = PermissionCheckLiveData(application, Manifest.permission.ACCESS_FINE_LOCATION)

    fun doPermissionCheck() = permissionCheck

    private val wifiStatusCheck = WifiStatusLiveData(application)

    fun doWifiStatusCheck() = wifiStatusCheck
}
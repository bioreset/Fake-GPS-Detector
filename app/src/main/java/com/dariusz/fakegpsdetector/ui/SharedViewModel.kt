package com.dariusz.fakegpsdetector.ui

import android.Manifest
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.dariusz.fakegpsdetector.gps.GpsStatusLiveData
import com.dariusz.fakegpsdetector.permissions.PermissionCheckLiveData
import com.dariusz.fakegpsdetector.wifistatus.WifiStatusLiveData

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    val gpsStatus = GpsStatusLiveData(application)

    val permissionCheck =
            PermissionCheckLiveData(application, listOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE))

    val wifiStatusCheck = WifiStatusLiveData(application)

}
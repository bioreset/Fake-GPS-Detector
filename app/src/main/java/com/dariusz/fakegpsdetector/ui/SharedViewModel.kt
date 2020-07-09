package com.dariusz.fakegpsdetector.ui

import android.Manifest
import android.content.Context
import androidx.lifecycle.ViewModel
import com.dariusz.fakegpsdetector.api.RestApiService
import com.dariusz.fakegpsdetector.datasource.GpsStatusLiveData
import com.dariusz.fakegpsdetector.datasource.PermissionStatusLiveData
import com.dariusz.fakegpsdetector.datasource.WifiStatusLiveData

class SharedViewModel(context: Context) : ViewModel() {

    val gpsStatus =
        GpsStatusLiveData(context)

    val permissionCheck =
        PermissionStatusLiveData(
            context,
            listOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE)
        )

    val wifiStatusCheck =
        WifiStatusLiveData(context)

    val service = RestApiService(context)
}
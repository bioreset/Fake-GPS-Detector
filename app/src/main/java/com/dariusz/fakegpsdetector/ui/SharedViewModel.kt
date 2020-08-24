package com.dariusz.fakegpsdetector.ui

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.dariusz.fakegpsdetector.di.DataSourceModule.provideGpsStatusLiveData
import com.dariusz.fakegpsdetector.di.DataSourceModule.providePermissionStatusLiveData
import com.dariusz.fakegpsdetector.di.DataSourceModule.provideWifiStatusLiveData

class SharedViewModel
@ViewModelInject
constructor() :
    ViewModel() {

    fun gpsStatus(context: Context) =
        provideGpsStatusLiveData(context)

    fun permissionCheck(context: Context, permissionToWatch: List<String>) =
        providePermissionStatusLiveData(
            context,
            permissionToWatch
        )

    fun wifiStatusCheck(context: Context) =
        provideWifiStatusLiveData(context)
}

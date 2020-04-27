package com.dariusz.fakegpsdetector.ui.firstscreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.dariusz.fakegpsdetector.location.LocationLiveData

class FirstScreenViewModel(application: Application) : AndroidViewModel(application) {

    private val locationData =
        LocationLiveData(
            application
        )

    fun getLocationData() = locationData

}
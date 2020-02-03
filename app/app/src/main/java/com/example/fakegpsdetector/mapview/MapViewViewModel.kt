package com.example.fakegpsdetector.mapview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.fakegpsdetector.mapview.location.LocationLiveData

class MapViewViewModel(application: Application) : AndroidViewModel(application) {

    private val locationData = LocationLiveData(application)

    fun getLocationData() = locationData

}
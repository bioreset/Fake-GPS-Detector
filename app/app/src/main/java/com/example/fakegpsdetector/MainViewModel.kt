package com.example.fakegpsdetector

import android.Manifest
import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import com.example.fakegpsdetector.location.*

class MainViewModel(application: Application) : AndroidViewModel(application){

    private val locationServiceListener = LocationServiceListener(application, Intent(application,
        LocationService::class.java))

    val gpsStatusLiveData = LocationStatusListener(application)

    val locationPermissionStatusLiveData = LocationPermissionStatusListener(application,
        Manifest.permission.ACCESS_FINE_LOCATION)

    private val locationData = LocationLiveData()

    fun getLocationData() = locationData

    fun startLocationTracking() = locationServiceListener.subscribeToLocationUpdates()

    fun stopLocationTracking() {
        locationServiceListener.unsubscribeFromLocationUpdates()
    }

}
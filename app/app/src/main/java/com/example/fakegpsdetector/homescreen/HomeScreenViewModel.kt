package com.example.fakegpsdetector.homescreen

import android.Manifest
import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import com.example.fakegpsdetector.location.LocationPermissionStatusListener
import com.example.fakegpsdetector.location.LocationService
import com.example.fakegpsdetector.location.LocationServiceListener
import com.example.fakegpsdetector.location.LocationStatusListener


class HomeScreenViewModel(application: Application) : AndroidViewModel(application) {

    private val locationServiceListener = LocationServiceListener(
        application, Intent(
            application,
            LocationService::class.java
        )
    )

    val gpsStatusLiveData = LocationStatusListener(application)

    val locationPermissionStatusLiveData = LocationPermissionStatusListener(
        application,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    fun startLocationTracking() = locationServiceListener.subscribeToLocationUpdates()

    fun stopLocationTracking() {
        locationServiceListener.unsubscribeFromLocationUpdates()
    }
}
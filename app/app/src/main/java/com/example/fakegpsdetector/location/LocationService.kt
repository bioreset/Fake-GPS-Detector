package com.example.fakegpsdetector.location

import android.Manifest
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Looper
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.fakegpsdetector.utils.combineLatestWith
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices

class LocationService : LifecycleService() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var locationRequest: LocationRequest

    private var gpsIsEnabled = false

    private var permissionIsGranted = false

    private lateinit var gpsAndPermissionStatusLiveData: LiveData<Pair<PermissionStatus, GpsStatus>>

    private val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: com.google.android.gms.location.LocationResult) {
        }
    }

    private val pairObserver = Observer<Pair<PermissionStatus, GpsStatus>> { pair ->
        pair?.let {
            handlePermissionStatus(pair.first)
            handleGpsStatus(pair.second)
            stopServiceIfNeeded()
        }
    }

    private fun handlePermissionStatus(status: PermissionStatus) {
        when (status) {
            is PermissionStatus.Granted -> {
                permissionIsGranted = true
                registerForLocationTracking()
            }

            is PermissionStatus.Denied -> {
                permissionIsGranted = false
            }
        }
    }

    private fun handleGpsStatus(status: GpsStatus) {
        when (status) {
            is GpsStatus.Enabled -> {
                gpsIsEnabled = true
                registerForLocationTracking()
            }

            is GpsStatus.Disabled -> {
                gpsIsEnabled = false
            }
        }
    }

    override fun onCreate() {
        super.onCreate()

        startObservingGpsAndPermissionStatus()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        gpsAndPermissionStatusLiveData = with(application) {
            LocationPermissionStatusListener(this, locationPermission)
                .combineLatestWith(LocationStatusListener(this))
        }

    }

    private fun startObservingGpsAndPermissionStatus() = gpsAndPermissionStatusLiveData
        .observe(this, pairObserver)

    private fun eitherPermissionOrGpsIsDisabled() = gpsIsEnabled.not() || permissionIsGranted.not()

    private fun registerForLocationTracking() {
        if (permissionIsGranted && gpsIsEnabled) {
            isTrackingRunning = try {
                fusedLocationClient.requestLocationUpdates(
                    locationRequest, locationCallback,
                    Looper.myLooper()
                )
                true
            } catch (unlikely: SecurityException) {
                error("Error when registerLocationUpdates()")
            }
        }
    }

    private fun unregisterFromLocationTracking() {
        try {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        } catch (unlikely: SecurityException) {
            error("Error when unregisterLocationUpdated()")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isTrackingRunning = false
        isServiceRunning = false

        if (eitherPermissionOrGpsIsDisabled().not()) unregisterFromLocationTracking()
    }

    private fun stopServiceIfNeeded() {
        if (eitherPermissionOrGpsIsDisabled()) {
            stopSelf()
        }
    }

    companion object {
        var isServiceRunning: Boolean = false
            private set

        var isTrackingRunning: Boolean = false
            private set
    }
}
package com.dariusz.fakegpsdetector.location

import android.content.Context
import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.dariusz.fakegpsdetector.model.LocationModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class LocationLiveData(private var context: Context) : MutableLiveData<LocationModel>() {

    private var fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    override fun onInactive() {
        super.onInactive()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    override fun onActive() {
        super.onActive()
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.also {
                    setLocationData(it)
                }
            }
        startLocationUpdates()
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            for (location in locationResult.locations) {
                setLocationData(location)
            }
        }
    }

    private fun setLocationData(location: Location) {
        postValue(LocationModel(
            longitude = location.longitude,
            latitude = location.latitude
        ))
    }

    private fun startLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    companion object {
        val locationRequest: LocationRequest = LocationRequest.create()
            .apply {
                interval = 1000L
                fastestInterval = 500L
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
    }
}

package com.example.fakegpsdetector.location

import androidx.lifecycle.LiveData


class LocationLiveData : LiveData<LocationModel>() {
}
data class LocationModel(
    val longitude: Double,
    val latitude: Double
)
package com.example.fakegpsdetector.location

interface LocationListener {
    fun subscribeToLocationUpdates()

    fun unsubscribeFromLocationUpdates()
}
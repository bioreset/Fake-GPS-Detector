package com.example.fakegpsdetector.location

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.os.Build

class LocationServiceListener(
    private val context: Context,
    private val serviceIntent: Intent
) : LocationListener {

    @TargetApi(Build.VERSION_CODES.O)
    override fun subscribeToLocationUpdates() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }
    }

    override fun unsubscribeFromLocationUpdates() {
        context.stopService(serviceIntent)
    }
}
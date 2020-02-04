package com.example.fakegpsdetector.location

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import com.example.fakegpsdetector.R

class LocationPermissionStatusListener(
    private val context: Context,
    private val permissionToListen: String
) : LiveData<PermissionStatus>() {

    override fun onActive() = handlePermissionCheck()

    private fun handlePermissionCheck() {
        val isPermissionGranted = ActivityCompat.checkSelfPermission(
            context,
            permissionToListen
        ) == PackageManager.PERMISSION_GRANTED

        if (isPermissionGranted)
            postValue(PermissionStatus.Granted())
        else
            postValue(PermissionStatus.Denied())
    }
}

sealed class PermissionStatus {
    data class Granted(val message: Int = R.string.permission_status_granted) : PermissionStatus()
    data class Denied(val message: Int = R.string.permission_status_denied) : PermissionStatus()
    data class Blocked(val message: Int = R.string.permission_status_blocked) : PermissionStatus()
}
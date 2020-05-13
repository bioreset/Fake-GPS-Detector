package com.dariusz.fakegpsdetector.permissions

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import com.dariusz.fakegpsdetector.model.PermissionStatusModel

class PermissionCheckLiveData(
        context: Context,
        permissionToListen: String
) : LiveData<PermissionStatusModel>() {

    private val isPermissionGranted = ActivityCompat.checkSelfPermission(
            context,
            permissionToListen
    ) == PackageManager.PERMISSION_GRANTED

    override fun onActive() = handlePermissionCheck()

    private fun handlePermissionCheck() {
        if (isPermissionGranted)
            postValue(PermissionStatusModel(status = true))
        else
            postValue(PermissionStatusModel(status = false))
    }
}

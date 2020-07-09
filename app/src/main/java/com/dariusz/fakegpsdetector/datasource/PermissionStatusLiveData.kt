package com.dariusz.fakegpsdetector.datasource

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import com.dariusz.fakegpsdetector.model.PermissionStatusModel
import kotlin.properties.Delegates

class PermissionStatusLiveData(
    private var context: Context,
    private var permissionsToListen: List<String>
) : LiveData<PermissionStatusModel>() {

    private var result by Delegates.notNull<Boolean>()

    private fun isPermissionGranted(): Boolean {
        for (permission in permissionsToListen) {
            result = ActivityCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
        return result
    }

    override fun onActive() = handlePermissionCheck()

    private fun handlePermissionCheck() {
        if (isPermissionGranted())
            postValue(PermissionStatusModel(status = true))
        else
            postValue(PermissionStatusModel(status = false))
    }
}

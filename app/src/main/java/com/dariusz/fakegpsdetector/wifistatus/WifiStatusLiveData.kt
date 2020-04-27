package com.dariusz.fakegpsdetector.wifistatus

import android.content.Context
import android.net.wifi.WifiManager
import androidx.lifecycle.LiveData
import com.dariusz.fakegpsdetector.R

class WifiStatusLiveData(private val context: Context): LiveData<WifiStatus>() {

    override fun onActive() = isWifiEnabled()

    private fun isWifiEnabled() {
        val isWifiOn = (context.getSystemService(Context.WIFI_SERVICE) as WifiManager).isWifiEnabled

        if (isWifiOn)
            postValue(WifiStatus.TurnedOn())
        else
            postValue(WifiStatus.TurnedOff())
    }
}

sealed class WifiStatus {
    data class TurnedOn(val message: Int = R.string.permission_status_granted) : WifiStatus()
    data class TurnedOff(val message: Int = R.string.permission_status_denied) : WifiStatus()
}
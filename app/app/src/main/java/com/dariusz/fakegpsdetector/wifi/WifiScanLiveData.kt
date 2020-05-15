package com.dariusz.fakegpsdetector.wifi

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import androidx.lifecycle.LiveData

class WifiScanLiveData(private var context: Context) : LiveData<List<ScanResult>>() {

    private val wifiScanResultsReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) = performScanning()
    }

    private var wifiManager: WifiManager =
            context.getSystemService(Context.WIFI_SERVICE) as WifiManager

    override fun onInactive() = unregisterReceiver()

    override fun onActive() {
        registerReceiver()
        performScanning()
    }

    private fun prepareScanning(): List<ScanResult>? {
        return wifiManager.scanResults as List<ScanResult>
    }

    private fun performScanning() {
        postValue(prepareScanning())
    }

    private fun registerReceiver() {
        context.registerReceiver(
                wifiScanResultsReceiver,
                IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        )
    }

    private fun unregisterReceiver() {
        context.unregisterReceiver(wifiScanResultsReceiver)
    }
}
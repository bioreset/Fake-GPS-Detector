package com.dariusz.fakegpsdetector.datasource

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.telephony.CellInfoLte
import android.telephony.TelephonyManager
import androidx.lifecycle.LiveData

class CellTowersLiveData(private var context: Context) : LiveData<List<CellInfoLte>?>() {

    private val wifiScanResultsReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) = prepareCellScan()
    }

    private val telephonyManager: TelephonyManager
        get() = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    @SuppressLint("MissingPermission")
    private var allCellInfo = telephonyManager.allCellInfo

    private var result: List<CellInfoLte>? = null

    override fun onActive() {
        registerReceiver()
        prepareCellScan()
    }

    override fun onInactive() {
        unregisterReceiver()
    }

    @Suppress("UNCHECKED_CAST")
    private fun castData(): List<CellInfoLte>? {
        result = allCellInfo as List<CellInfoLte>
        return result
    }

    private fun prepareCellScan() {
        postValue(castData())
    }

    private fun registerReceiver() {
        context.registerReceiver(
            wifiScanResultsReceiver,
            IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED)
        )
    }

    private fun unregisterReceiver() {
        context.unregisterReceiver(wifiScanResultsReceiver)
    }

}


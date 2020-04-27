package com.dariusz.fakegpsdetector.celltowers

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.CellInfo
import android.telephony.CellInfoGsm
import android.telephony.TelephonyManager
import androidx.lifecycle.LiveData

class CellTowersLiveData(private var context: Context) : LiveData<List<CellInfoGsm>>() {

    private val wifiScanResultsReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) = returnData()
    }

    private val telephonyManager: TelephonyManager
        get() = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    override fun onActive() {
        returnData()
    }

    @SuppressLint("MissingPermission")
    private fun prepareManager(): List<CellInfo>? {
        return telephonyManager.allCellInfo as List<CellInfo>
    }

    @Suppress("UNCHECKED_CAST")
    private fun castList(): List<CellInfoGsm> {
        return prepareManager() as List<CellInfoGsm>
    }

    private fun returnData() {
        value = castList()
    }

}
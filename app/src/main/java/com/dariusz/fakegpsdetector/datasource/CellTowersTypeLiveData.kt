package com.dariusz.fakegpsdetector.datasource

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.telephony.TelephonyManager
import androidx.lifecycle.LiveData

class CellTowersTypeLiveData(private var context: Context) : LiveData<Int>() {

    private val cellTowersTypeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) = onCellInfoChange()
    }

    private val telephonyManager: TelephonyManager
        get() = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    @SuppressLint("MissingPermission")
    private var allCellInfo = telephonyManager.dataNetworkType

    override fun onActive() {
        registerReceiver()
        postValue(allCellInfo)
    }

    override fun onInactive() {
        unregisterReceiver()
    }

    private fun onCellInfoChange() {
        allCellInfo
    }

    private fun registerReceiver() {
        context.registerReceiver(
            cellTowersTypeReceiver,
            IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED)
        )
    }

    private fun unregisterReceiver() {
        context.unregisterReceiver(cellTowersTypeReceiver)
    }
}

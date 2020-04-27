package com.dariusz.fakegpsdetector.model

import android.net.wifi.ScanResult

data class RouterModel(
    val ssid: String?,
    val bssid: String? = null,
    val frequency: Int? = null,
    val level: Int? = null
){
    companion object {
        fun newRoutersList(rmdl:List<ScanResult>): List<RouterModel> {
            return rmdl.map { RouterModel(
                ssid = it.SSID ,
                bssid = it.BSSID,
                frequency = it.frequency,
                level = it.level
            )}
        }
    }
}
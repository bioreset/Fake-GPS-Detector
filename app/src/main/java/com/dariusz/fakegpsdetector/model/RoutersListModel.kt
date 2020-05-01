package com.dariusz.fakegpsdetector.model

import android.net.wifi.ScanResult

data class RoutersListModel(
    val ssid: String?,
    val bssid: String? = null,
    val frequency: Int? = null,
    val level: Int? = null
) {
    companion object {
        fun newRoutersList(rmdl: List<ScanResult>): List<RoutersListModel> {
            return rmdl.map {
                RoutersListModel(
                    ssid = it.SSID,
                    bssid = it.BSSID,
                    frequency = it.frequency,
                    level = it.level
                )
            }
        }
    }
}
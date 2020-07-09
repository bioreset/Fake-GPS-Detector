package com.dariusz.fakegpsdetector.ui.secondscreen

import android.content.Context
import androidx.lifecycle.ViewModel
import com.dariusz.fakegpsdetector.datasource.WifiScanResultsLiveData
import com.dariusz.fakegpsdetector.repository.RoutersListRepository

class SecondScreenViewModel(
    context: Context,
    routersListRepository: RoutersListRepository
) : ViewModel() {

    val wifiData =
        WifiScanResultsLiveData(
            context
        )

    val repo = routersListRepository
}
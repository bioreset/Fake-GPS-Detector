package com.dariusz.fakegpsdetector.ui.secondscreen

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.dariusz.fakegpsdetector.di.DataSourceModule.provideWifiScanResultsLiveData
import com.dariusz.fakegpsdetector.repository.RoutersListRepository
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class SecondScreenViewModel
@ViewModelInject
constructor(
    routersListRepository: RoutersListRepository
) : ViewModel() {

    fun wifiData(context: Context) =
        provideWifiScanResultsLiveData(
            context
        )

    val repo = routersListRepository
}

package com.dariusz.fakegpsdetector.ui.thirdscreen

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.dariusz.fakegpsdetector.di.DataSourceModule.provideCellTowersLiveData
import com.dariusz.fakegpsdetector.di.DataSourceModule.provideCellTowersTypeLiveData
import com.dariusz.fakegpsdetector.repository.CellTowersRepository

class ThirdScreenViewModel
@ViewModelInject
constructor(
    cellTowersRepository: CellTowersRepository
) : ViewModel() {

    fun cellTowersData(context: Context) =
        provideCellTowersLiveData(context)

    fun cellTowersType(context: Context) = provideCellTowersTypeLiveData(context)

    val repo = cellTowersRepository
}
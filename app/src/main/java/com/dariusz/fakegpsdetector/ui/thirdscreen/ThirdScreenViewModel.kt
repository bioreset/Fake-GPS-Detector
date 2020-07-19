package com.dariusz.fakegpsdetector.ui.thirdscreen

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.dariusz.fakegpsdetector.di.DataSourceModule.provideCellTowersLiveData
import com.dariusz.fakegpsdetector.repository.CellTowersRepository
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class ThirdScreenViewModel
@ViewModelInject
constructor(
    cellTowersRepository: CellTowersRepository
) : ViewModel() {

    fun cellTowersData(context: Context) =
        provideCellTowersLiveData(context)

    val repo = cellTowersRepository
}

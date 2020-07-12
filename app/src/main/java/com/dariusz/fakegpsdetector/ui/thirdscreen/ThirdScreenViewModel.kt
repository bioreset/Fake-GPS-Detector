package com.dariusz.fakegpsdetector.ui.thirdscreen

import android.content.Context
import androidx.lifecycle.ViewModel
import com.dariusz.fakegpsdetector.datasource.CellTowersLiveData
import com.dariusz.fakegpsdetector.datasource.CellTowersTypeLiveData
import com.dariusz.fakegpsdetector.repository.CellTowersRepository

class ThirdScreenViewModel(
    context: Context,
    cellTowersRepository: CellTowersRepository
) : ViewModel() {

    val cellTowersData =
        CellTowersLiveData(context)

    val cellTowersType = CellTowersTypeLiveData(context)

    val repo = cellTowersRepository
}
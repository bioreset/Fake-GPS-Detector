package com.dariusz.fakegpsdetector.ui.thirdscreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.dariusz.fakegpsdetector.celltowers.CellTowersLiveData

class ThirdScreenViewModel(application: Application) : AndroidViewModel(application) {

    private val cellTowersData = CellTowersLiveData(application)

    fun getCellTowersData() = cellTowersData
}
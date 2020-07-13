package com.dariusz.fakegpsdetector.ui.firstscreen

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.dariusz.fakegpsdetector.di.DataSourceModule.provideLocationLiveData
import com.dariusz.fakegpsdetector.repository.LocationFromApiResponseRepository
import com.dariusz.fakegpsdetector.repository.LocationRepository

class FirstScreenViewModel
@ViewModelInject
constructor(
    locationRepository: LocationRepository,
    locationFromApiResponseRepository: LocationFromApiResponseRepository
) : ViewModel() {

    fun locationData(context: Context) =
        provideLocationLiveData(context)

    val repoLocation = locationRepository

    val repoResult = locationFromApiResponseRepository


}
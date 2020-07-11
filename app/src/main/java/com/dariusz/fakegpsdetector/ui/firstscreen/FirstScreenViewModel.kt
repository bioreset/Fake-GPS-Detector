package com.dariusz.fakegpsdetector.ui.firstscreen

import android.content.Context
import androidx.lifecycle.ViewModel
import com.dariusz.fakegpsdetector.datasource.LocationLiveData
import com.dariusz.fakegpsdetector.repository.LocationFromApiResponseRepository
import com.dariusz.fakegpsdetector.repository.LocationRepository

class FirstScreenViewModel(
    context: Context,
    locationRepository: LocationRepository,
    locationFromApiResponseRepository: LocationFromApiResponseRepository
) : ViewModel() {

    val locationData =
        LocationLiveData(
            context
        )

    val repoLocation = locationRepository

    val repoResult = locationFromApiResponseRepository


}
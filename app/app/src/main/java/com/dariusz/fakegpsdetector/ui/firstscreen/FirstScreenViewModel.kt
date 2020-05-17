package com.dariusz.fakegpsdetector.ui.firstscreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.dariusz.fakegpsdetector.location.LocationLiveData
import com.dariusz.fakegpsdetector.repository.LocationFromApiResponseRepository
import com.dariusz.fakegpsdetector.repository.LocationRepository

class FirstScreenViewModel(application: Application) : AndroidViewModel(application) {

    private val locationData =
            LocationLiveData(
                    application
            )

    fun getLocationData() = locationData

    var repo_location = LocationRepository.getInstance(application)

    var repo_result = LocationFromApiResponseRepository.getInstance(application)

}
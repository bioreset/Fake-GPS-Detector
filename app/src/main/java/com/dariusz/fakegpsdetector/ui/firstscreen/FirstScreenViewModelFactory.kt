package com.dariusz.fakegpsdetector.ui.firstscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dariusz.fakegpsdetector.repository.LocationFromApiResponseRepository
import com.dariusz.fakegpsdetector.repository.LocationRepository
import javax.inject.Inject

class FirstScreenViewModelFactory
@Inject
constructor(
    private val locationRepository: LocationRepository,
    private val locationFromApiResponseRepository: LocationFromApiResponseRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FirstScreenViewModel(
            locationRepository,
            locationFromApiResponseRepository
        ) as T
    }
}

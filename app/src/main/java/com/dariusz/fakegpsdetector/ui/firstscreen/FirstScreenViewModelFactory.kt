package com.dariusz.fakegpsdetector.ui.firstscreen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dariusz.fakegpsdetector.repository.LocationFromApiResponseRepository
import com.dariusz.fakegpsdetector.repository.LocationRepository

class FirstScreenViewModelFactory(
    private val context: Context,
    private val locationRepository: LocationRepository,
    private val locationFromApiResponseRepository: LocationFromApiResponseRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FirstScreenViewModel(
            context,
            locationRepository,
            locationFromApiResponseRepository
        ) as T
    }
}
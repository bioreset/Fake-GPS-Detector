package com.dariusz.fakegpsdetector.ui.thirdscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dariusz.fakegpsdetector.repository.CellTowersRepository
import javax.inject.Inject

class ThirdScreenViewModelFactory
@Inject
constructor(
    private val cellTowersRepository: CellTowersRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ThirdScreenViewModel(cellTowersRepository) as T
    }
}
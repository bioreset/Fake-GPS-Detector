package com.dariusz.fakegpsdetector.ui.thirdscreen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dariusz.fakegpsdetector.repository.CellTowersRepository

class ThirdScreenViewModelFactory(
    private val context: Context,
    private val cellTowersRepository: CellTowersRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ThirdScreenViewModel(context, cellTowersRepository) as T
    }
}
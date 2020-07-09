package com.dariusz.fakegpsdetector.ui.secondscreen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dariusz.fakegpsdetector.repository.RoutersListRepository

class SecondScreenViewModelFactory(
    private val context: Context,
    private val routersListRepository: RoutersListRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SecondScreenViewModel(context, routersListRepository) as T
    }
}
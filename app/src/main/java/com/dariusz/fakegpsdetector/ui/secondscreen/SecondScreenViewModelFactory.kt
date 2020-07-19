package com.dariusz.fakegpsdetector.ui.secondscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dariusz.fakegpsdetector.repository.RoutersListRepository
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

@InternalCoroutinesApi
class SecondScreenViewModelFactory
@Inject
constructor(
    private val routersListRepository: RoutersListRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SecondScreenViewModel(routersListRepository) as T
    }
}

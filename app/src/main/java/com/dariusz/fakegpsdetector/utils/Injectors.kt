package com.dariusz.fakegpsdetector.utils

import android.content.Context
import com.dariusz.fakegpsdetector.di.CacheModule.provideCellTowersDAO
import com.dariusz.fakegpsdetector.di.CacheModule.provideLocationDAO
import com.dariusz.fakegpsdetector.di.CacheModule.provideLocationFromApiResponseDAO
import com.dariusz.fakegpsdetector.di.CacheModule.provideRoutersListDAO
import com.dariusz.fakegpsdetector.di.NetworkModule.provideRetrofitService
import com.dariusz.fakegpsdetector.repository.CellTowersRepository
import com.dariusz.fakegpsdetector.repository.LocationFromApiResponseRepository
import com.dariusz.fakegpsdetector.repository.LocationRepository
import com.dariusz.fakegpsdetector.repository.RoutersListRepository
import com.dariusz.fakegpsdetector.ui.SharedViewModelFactory
import com.dariusz.fakegpsdetector.ui.firstscreen.FirstScreenViewModelFactory
import com.dariusz.fakegpsdetector.ui.secondscreen.SecondScreenViewModelFactory
import com.dariusz.fakegpsdetector.ui.thirdscreen.ThirdScreenViewModelFactory
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
object Injectors {

    private fun getCellTowersRepository(context: Context): CellTowersRepository {
        return CellTowersRepository(
            provideCellTowersDAO(context)
        )
    }

    private fun getLocationFromApiResponseRepository(context: Context): LocationFromApiResponseRepository {
        return LocationFromApiResponseRepository(
            provideRetrofitService(),
            provideLocationFromApiResponseDAO(context)
        )
    }

    private fun getLocationRepository(context: Context): LocationRepository {
        return LocationRepository(
            provideLocationDAO(context)
        )
    }

    private fun getRoutersListRepository(context: Context): RoutersListRepository {
        return RoutersListRepository(
            provideRoutersListDAO(context)
        )
    }

    fun provideSharedViewModelFactory(): SharedViewModelFactory {
        return SharedViewModelFactory()
    }

    fun provideFirstScreenViewModelFactory(
        context: Context
    ): FirstScreenViewModelFactory {
        return FirstScreenViewModelFactory(
            getLocationRepository(context),
            getLocationFromApiResponseRepository(context)
        )
    }

    fun provideSecondScreenViewModelFactory(
        context: Context
    ): SecondScreenViewModelFactory {
        return SecondScreenViewModelFactory(getRoutersListRepository(context))
    }

    fun provideThirdScreenViewModelFactory(
        context: Context
    ): ThirdScreenViewModelFactory {
        return ThirdScreenViewModelFactory(getCellTowersRepository(context))
    }
}

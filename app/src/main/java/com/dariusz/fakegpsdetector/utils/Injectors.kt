package com.dariusz.fakegpsdetector.utils

import android.content.Context
import com.dariusz.fakegpsdetector.api.FakeGPSRestApiService
import com.dariusz.fakegpsdetector.db.init.FGDDatabase
import com.dariusz.fakegpsdetector.repository.CellTowersRepository
import com.dariusz.fakegpsdetector.repository.LocationFromApiResponseRepository
import com.dariusz.fakegpsdetector.repository.LocationRepository
import com.dariusz.fakegpsdetector.repository.RoutersListRepository
import com.dariusz.fakegpsdetector.ui.SharedViewModelFactory
import com.dariusz.fakegpsdetector.ui.firstscreen.FirstScreenViewModelFactory
import com.dariusz.fakegpsdetector.ui.secondscreen.SecondScreenViewModelFactory
import com.dariusz.fakegpsdetector.ui.thirdscreen.ThirdScreenViewModelFactory

object Injectors {

    fun getCellTowersRepository(context: Context): CellTowersRepository {
        return CellTowersRepository.getInstance(
            FGDDatabase.getInstance(context.applicationContext).cellTowersDao()
        )
    }

    fun getLocationFromApiResponseRepository(context: Context): LocationFromApiResponseRepository {
        return LocationFromApiResponseRepository.getInstance(
            FakeGPSRestApiService(),
            FGDDatabase.getInstance(context.applicationContext).locationFromApiResponseDao()
        )
    }

    fun getLocationRepository(context: Context): LocationRepository {
        return LocationRepository.getInstance(
            FGDDatabase.getInstance(context.applicationContext).locationDao()
        )
    }

    fun getRoutersListRepository(context: Context): RoutersListRepository {
        return RoutersListRepository.getInstance(
            FGDDatabase.getInstance(context.applicationContext).routersListDao()
        )
    }

    fun provideSharedViewModelFactory(
        context: Context
    ): SharedViewModelFactory {
        return SharedViewModelFactory(context)
    }

    fun provideFirstScreenViewModelFactory(
        context: Context
    ): FirstScreenViewModelFactory {
        return FirstScreenViewModelFactory(
            context,
            getLocationRepository(context),
            getLocationFromApiResponseRepository(context)
        )
    }

    fun provideSecondScreenViewModelFactory(
        context: Context
    ): SecondScreenViewModelFactory {
        return SecondScreenViewModelFactory(context, getRoutersListRepository(context))
    }

    fun provideThirdScreenViewModelFactory(
        context: Context
    ): ThirdScreenViewModelFactory {
        return ThirdScreenViewModelFactory(context, getCellTowersRepository(context))
    }


}
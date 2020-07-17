package com.dariusz.fakegpsdetector.di

import android.content.Context
import com.dariusz.fakegpsdetector.datasource.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DataSourceModule {

    @Singleton
    @Provides
    fun provideCellTowersLiveData(@ApplicationContext context: Context): CellTowersLiveData {
        return CellTowersLiveData(context)
    }

    @Singleton
    @Provides
    fun provideGpsStatusLiveData(@ApplicationContext context: Context): GpsStatusLiveData {
        return GpsStatusLiveData(context)
    }

    @Singleton
    @Provides
    fun provideLocationLiveData(@ApplicationContext context: Context): LocationLiveData {
        return LocationLiveData(context)
    }

    @Singleton
    @Provides
    fun providePermissionStatusLiveData(
        @ApplicationContext context: Context,
        permissionsToListen: List<String>
    ): PermissionStatusLiveData {
        return PermissionStatusLiveData(context, permissionsToListen)
    }

    @Singleton
    @Provides
    fun provideWifiScanResultsLiveData(@ApplicationContext context: Context): WifiScanResultsLiveData {
        return WifiScanResultsLiveData(context)
    }

    @Singleton
    @Provides
    fun provideWifiStatusLiveData(@ApplicationContext context: Context): WifiStatusLiveData {
        return WifiStatusLiveData(context)
    }
}

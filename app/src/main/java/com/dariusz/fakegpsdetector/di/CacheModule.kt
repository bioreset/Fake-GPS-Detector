package com.dariusz.fakegpsdetector.di

import android.content.Context
import androidx.room.Room
import com.dariusz.fakegpsdetector.db.dao.CellTowersDao
import com.dariusz.fakegpsdetector.db.dao.LocationDao
import com.dariusz.fakegpsdetector.db.dao.LocationFromApiResponseDao
import com.dariusz.fakegpsdetector.db.dao.RoutersListDao
import com.dariusz.fakegpsdetector.db.init.FGDDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object CacheModule {

    @Singleton
    @Provides
    fun buildDatabase(@ApplicationContext context: Context): FGDDatabase {
        return Room.databaseBuilder(context, FGDDatabase::class.java, "fgd_database_official")
            .build()
    }

    @Singleton
    @Provides
    fun provideCellTowersDAO(@ApplicationContext context: Context): CellTowersDao {
        return FGDDatabase.getInstance(context.applicationContext).cellTowersDao()
    }

    @Singleton
    @Provides
    fun provideLocationDAO(@ApplicationContext context: Context): LocationDao {
        return FGDDatabase.getInstance(context.applicationContext).locationDao()
    }

    @Singleton
    @Provides
    fun provideLocationFromApiResponseDAO(@ApplicationContext context: Context): LocationFromApiResponseDao {
        return FGDDatabase.getInstance(context.applicationContext).locationFromApiResponseDao()
    }

    @Singleton
    @Provides
    fun provideRoutersListDAO(@ApplicationContext context: Context): RoutersListDao {
        return FGDDatabase.getInstance(context.applicationContext).routersListDao()
    }


}
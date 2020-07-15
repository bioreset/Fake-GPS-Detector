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

@Module
@InstallIn(ApplicationComponent::class)
object CacheModule {

    @Provides
    fun buildDatabase(@ApplicationContext context: Context): FGDDatabase {
        return Room.databaseBuilder(context, FGDDatabase::class.java, "fgd_database_official")
            .build()
    }

    @Provides
    fun provideCellTowersDAO(@ApplicationContext context: Context): CellTowersDao {
        return buildDatabase(context.applicationContext).cellTowersDao()
    }

    @Provides
    fun provideLocationDAO(@ApplicationContext context: Context): LocationDao {
        return buildDatabase(context.applicationContext).locationDao()
    }

    @Provides
    fun provideLocationFromApiResponseDAO(@ApplicationContext context: Context): LocationFromApiResponseDao {
        return buildDatabase(context.applicationContext).locationFromApiResponseDao()
    }

    @Provides
    fun provideRoutersListDAO(@ApplicationContext context: Context): RoutersListDao {
        return buildDatabase(context.applicationContext).routersListDao()
    }
}

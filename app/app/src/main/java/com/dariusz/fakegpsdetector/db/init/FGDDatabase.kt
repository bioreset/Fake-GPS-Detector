package com.dariusz.fakegpsdetector.db.init

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dariusz.fakegpsdetector.api.apimodel.ApiResponseModel
import com.dariusz.fakegpsdetector.db.dao.*
import com.dariusz.fakegpsdetector.model.CellTowerModel
import com.dariusz.fakegpsdetector.model.LocationModel
import com.dariusz.fakegpsdetector.model.RoutersListModel

@Database(entities = [CellTowerModel::class, LocationModel::class, RoutersListModel::class, ApiResponseModel::class], version = 8, exportSchema = false)
abstract class FGDDatabase : RoomDatabase() {

    abstract fun locationDao(): LocationDao
    abstract fun routersListDao(): RoutersListDao
    abstract fun cellTowersDao(): CellTowersDao
    abstract fun locationFromApiResponseDao(): LocationFromApiResponseDao

    companion object {

        @Volatile
        private var instance: FGDDatabase? = null

        fun getInstanceOf(context: Context): FGDDatabase? {
            if (instance == null) {
                synchronized(FGDDatabase::class) {
                    instance = Room.databaseBuilder(
                            context.applicationContext,
                            FGDDatabase::class.java, "fgd_database_prod"
                    ).fallbackToDestructiveMigration().allowMainThreadQueries().build()
                }
            }
            return instance
        }

    }


}
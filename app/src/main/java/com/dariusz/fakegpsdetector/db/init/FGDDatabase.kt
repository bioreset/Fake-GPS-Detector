package com.dariusz.fakegpsdetector.db.init

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import com.dariusz.fakegpsdetector.db.dao.CellTowersDao
import com.dariusz.fakegpsdetector.db.dao.LocationDao
import com.dariusz.fakegpsdetector.db.dao.LocationFromApiResponseDao
import com.dariusz.fakegpsdetector.db.dao.RoutersListDao
import com.dariusz.fakegpsdetector.di.CacheModule.buildDatabase
import com.dariusz.fakegpsdetector.model.ApiResponseModel
import com.dariusz.fakegpsdetector.model.CellTowerModel
import com.dariusz.fakegpsdetector.model.LocationModel
import com.dariusz.fakegpsdetector.model.RoutersListModel

@Database(
    entities = [CellTowerModel::class, LocationModel::class, RoutersListModel::class, ApiResponseModel::class],
    version = 1,
    exportSchema = false
)
abstract class FGDDatabase : RoomDatabase() {

    abstract fun locationDao(): LocationDao
    abstract fun routersListDao(): RoutersListDao
    abstract fun cellTowersDao(): CellTowersDao
    abstract fun locationFromApiResponseDao(): LocationFromApiResponseDao

    companion object {

        @Volatile
        private var instance: FGDDatabase? = null

        fun getInstance(context: Context): FGDDatabase {
            return instance ?: synchronized(this) {
                instance
                    ?: buildDatabase(
                        context
                    ).also { instance = it }
            }
        }
    }
}

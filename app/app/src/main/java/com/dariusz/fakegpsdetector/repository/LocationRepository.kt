package com.dariusz.fakegpsdetector.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.dariusz.fakegpsdetector.db.dao.LocationDao
import com.dariusz.fakegpsdetector.db.init.FGDDatabase
import com.dariusz.fakegpsdetector.model.LocationModel

class LocationRepository(context: Context) {

    private lateinit var locationDao: LocationDao

    fun insert(location: LocationModel) {
        deleteAll()
        insertAsync(location)
    }

    fun selectAll(): LiveData<LocationModel> {
        return locationDao.getLocation()
    }

    private fun insertAsync(location: LocationModel) {
        Thread(Runnable {
            locationDao.insert(location)
        }).start()
    }

    private fun deleteAll() {
        locationDao.deleteAllLocationInfo()
    }

    init {
        val db: FGDDatabase? = FGDDatabase.getInstanceOf(context)
        if (db != null) {
            locationDao = db.locationDao()
        }
    }

    companion object {
        @Volatile
        private var instance: LocationRepository? = null

        fun getInstance(context: Context): LocationRepository =
                instance ?: synchronized(this) {
                    instance ?: LocationRepository(context).also { instance = it }
                }
    }
}

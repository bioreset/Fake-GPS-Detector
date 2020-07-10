package com.dariusz.fakegpsdetector.repository

import androidx.lifecycle.asLiveData
import com.dariusz.fakegpsdetector.db.dao.LocationDao
import com.dariusz.fakegpsdetector.model.LocationModel
import com.dariusz.fakegpsdetector.utils.RepositoryUtils.performCacheCall

class LocationRepository
constructor(
    private val locationDao: LocationDao
) {

    suspend fun insertAsFresh(location: LocationModel) {
        deleteAll()
        performCacheCall(locationDao.insert(location))
    }

    suspend fun selectAll() = performCacheCall(locationDao.getLocation()).asLiveData().value

    private suspend fun deleteAll() = performCacheCall(locationDao.deleteAllLocationInfo())

    companion object {

        @Volatile
        private var instance: LocationRepository? = null

        fun getInstance(
            locationDao: LocationDao
        ) =
            instance ?: synchronized(this) {
                instance ?: LocationRepository(
                    locationDao
                ).also { instance = it }
            }
    }
}

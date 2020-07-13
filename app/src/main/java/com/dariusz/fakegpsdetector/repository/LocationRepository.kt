package com.dariusz.fakegpsdetector.repository

import androidx.lifecycle.asLiveData
import com.dariusz.fakegpsdetector.db.dao.LocationDao
import com.dariusz.fakegpsdetector.model.LocationModel
import com.dariusz.fakegpsdetector.utils.RepositoryUtils.performCacheCall
import javax.inject.Inject

class LocationRepository
@Inject
constructor(
    private val locationDao: LocationDao
) {

    suspend fun insertAsFresh(location: LocationModel) {
        deleteAll()
        performCacheCall(locationDao.insert(location))
    }

    suspend fun selectAll() = performCacheCall(locationDao.getLocation()).asLiveData().value

    private suspend fun deleteAll() = performCacheCall(locationDao.deleteAllLocationInfo())

}

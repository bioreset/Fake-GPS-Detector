package com.dariusz.fakegpsdetector.repository

import androidx.lifecycle.asLiveData
import com.dariusz.fakegpsdetector.db.dao.RoutersListDao
import com.dariusz.fakegpsdetector.model.RoutersListModel
import com.dariusz.fakegpsdetector.utils.RepositoryUtils.performCacheCall
import javax.inject.Inject

class RoutersListRepository
@Inject
constructor(
    private val routersListDao: RoutersListDao
) {

    suspend fun insertAsFresh(routerList: List<RoutersListModel>) {
        deleteAll()
        performCacheCall(routersListDao.insertAll(routerList))
    }

    suspend fun selectAll() =
        performCacheCall(routersListDao.getAllRouters()).asLiveData().value

    private suspend fun deleteAll() = performCacheCall(routersListDao.deleteAllRouters())
}

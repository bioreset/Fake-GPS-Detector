package com.dariusz.fakegpsdetector.repository

import com.dariusz.fakegpsdetector.db.dao.RoutersListDao
import com.dariusz.fakegpsdetector.model.RoutersListModel
import com.dariusz.fakegpsdetector.utils.RepositoryUtils.performCacheCall
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RoutersListRepository
@Inject
constructor(
    private val routersListDao: RoutersListDao
) {

    suspend fun insertAsFresh(routerList: List<RoutersListModel>) = flow<Unit> {
        deleteAll()
        performCacheCall(routersListDao.insertAll(routerList))
    }

    suspend fun selectAll() =
        performCacheCall(routersListDao.getAllRouters())

    private suspend fun deleteAll() = performCacheCall(routersListDao.deleteAllRouters())
}

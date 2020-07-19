package com.dariusz.fakegpsdetector.repository

import com.dariusz.fakegpsdetector.db.dao.RoutersListDao
import com.dariusz.fakegpsdetector.model.RoutersListModel
import com.dariusz.fakegpsdetector.utils.RepositoryUtils.performCacheCall
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

@InternalCoroutinesApi
class RoutersListRepository
@Inject
constructor(
    private val routersListDao: RoutersListDao
) {

    suspend fun insertAsFresh(routerList: List<RoutersListModel>) {
        performCacheCall(routersListDao.deleteAllRouters())
        performCacheCall(routersListDao.insertAll(routerList))
    }

    suspend fun selectAll() =
        performCacheCall(routersListDao.getAllRouters())
}

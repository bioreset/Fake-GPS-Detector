package com.dariusz.fakegpsdetector.repository

import com.dariusz.fakegpsdetector.db.dao.RoutersListDao
import com.dariusz.fakegpsdetector.model.RoutersListModel
import com.dariusz.fakegpsdetector.utils.RepositoryUtils.performCacheCall

class RoutersListRepository
constructor(
    private val routersListDao: RoutersListDao
) {

    suspend fun insertAsFresh(routerList: List<RoutersListModel>) {
        deleteAll()
        performCacheCall(routersListDao.insertAll(routerList))
    }

    suspend fun selectAll(): List<RoutersListModel>? =
        performCacheCall(routersListDao.getAllRouters()).value

    private suspend fun deleteAll() = performCacheCall(routersListDao.deleteAllRouters())

    companion object {

        @Volatile
        private var instance: RoutersListRepository? = null

        fun getInstance(
            routersListDao: RoutersListDao
        ) =
            instance ?: synchronized(this) {
                instance ?: RoutersListRepository(
                    routersListDao
                ).also { instance = it }
            }
    }
}
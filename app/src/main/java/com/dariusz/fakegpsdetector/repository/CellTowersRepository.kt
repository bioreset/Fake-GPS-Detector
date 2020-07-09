package com.dariusz.fakegpsdetector.repository

import com.dariusz.fakegpsdetector.db.dao.CellTowersDao
import com.dariusz.fakegpsdetector.model.CellTowerModel
import com.dariusz.fakegpsdetector.utils.RepositoryUtils.performCacheCall

class CellTowersRepository
constructor(
    private val cellTowersDao: CellTowersDao
) {
    suspend fun insertAsFresh(cellTowerList: List<CellTowerModel>) {
        deleteAll()
        performCacheCall(cellTowersDao.insertAll(cellTowerList))
    }

    suspend fun selectAll(): List<CellTowerModel>? =
        performCacheCall(cellTowersDao.getAllCellTowers()).value

    private suspend fun deleteAll() = performCacheCall(cellTowersDao.deleteAllCellTowers())

    companion object {

        @Volatile
        private var instance: CellTowersRepository? = null

        fun getInstance(
            cellTowersDao: CellTowersDao
        ) =
            instance ?: synchronized(this) {
                instance ?: CellTowersRepository(
                    cellTowersDao
                ).also { instance = it }
            }
    }
}

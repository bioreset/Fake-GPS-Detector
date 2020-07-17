package com.dariusz.fakegpsdetector.repository

import com.dariusz.fakegpsdetector.db.dao.CellTowersDao
import com.dariusz.fakegpsdetector.model.CellTowerModel
import com.dariusz.fakegpsdetector.utils.RepositoryUtils.performCacheCall
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CellTowersRepository
@Inject
constructor(
    private val cellTowersDao: CellTowersDao
) {

    suspend fun insertAsFresh(cellTowerList: List<CellTowerModel>) = flow<Unit> {
        deleteAll()
        performCacheCall(cellTowersDao.insertAll(cellTowerList))
    }

    suspend fun selectAll() =
        performCacheCall(cellTowersDao.getAllCellTowers())

    private suspend fun deleteAll() = performCacheCall(cellTowersDao.deleteAllCellTowers())
}

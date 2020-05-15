package com.dariusz.fakegpsdetector.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.dariusz.fakegpsdetector.db.dao.CellTowersDao
import com.dariusz.fakegpsdetector.db.init.FGDDatabase
import com.dariusz.fakegpsdetector.model.CellTowerModel

class CellTowersRepository(context: Context) {

    private lateinit var cellTowersDao: CellTowersDao

    fun insert(cellTowerList: List<CellTowerModel>) {
        deleteAll()
        insertAsync(cellTowerList)
    }

    fun selectAll(): LiveData<List<CellTowerModel>> {
        return cellTowersDao.getAllCellTowers()
    }

    private fun insertAsync(cellTowerList: List<CellTowerModel>) {
        Thread(Runnable {
            cellTowersDao.insertAll(cellTowerList)
        }).start()
    }

    private fun deleteAll() {
        cellTowersDao.deleteAllCellTowers()
    }

    init {
        val db: FGDDatabase? = FGDDatabase.getInstanceOf(context)
        if (db != null) {
            cellTowersDao = db.cellTowersDao()
        }
    }

    companion object {
        @Volatile
        private var instance: CellTowersRepository? = null

        fun getInstance(context: Context): CellTowersRepository =
                instance ?: synchronized(this) {
                    instance ?: CellTowersRepository(context).also { instance = it }
                }
    }
}

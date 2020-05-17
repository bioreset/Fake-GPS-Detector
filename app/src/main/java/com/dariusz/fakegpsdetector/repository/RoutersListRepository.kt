package com.dariusz.fakegpsdetector.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.dariusz.fakegpsdetector.db.dao.RoutersListDao
import com.dariusz.fakegpsdetector.db.init.FGDDatabase
import com.dariusz.fakegpsdetector.model.RoutersListModel

class RoutersListRepository(context: Context) {

    private lateinit var routersListDao: RoutersListDao

    fun insert(routerList: List<RoutersListModel>) {
        deleteAll()
        insertAsync(routerList)
    }

    fun selectAll(): List<RoutersListModel> {
        return routersListDao.getAllRouters()
    }

    private fun insertAsync(routerList: List<RoutersListModel>) {
        Thread(Runnable {
            routersListDao.insertAll(routerList)
        }).start()
    }

    private fun deleteAll() {
        routersListDao.deleteAllRouters()
    }

    init {
        val db: FGDDatabase? = FGDDatabase.getInstanceOf(context)
        if (db != null) {
            routersListDao = db.routersListDao()
        }
    }

    companion object {
        @Volatile
        private var instance: RoutersListRepository? = null

        fun getInstance(context: Context): RoutersListRepository =
                instance ?: synchronized(this) {
                    instance ?: RoutersListRepository(context).also { instance = it }
                }
    }
}
package com.dariusz.fakegpsdetector.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dariusz.fakegpsdetector.model.RoutersListModel

@Dao
interface RoutersListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insertAll(cellTowerList: List<RoutersListModel>)

    @Query("DELETE FROM routers_table")
    fun deleteAllRouters()

    @Query("SELECT id, ssid, macAddress, frequency, level FROM routers_table ")
    fun getAllRouters(): LiveData<List<RoutersListModel>>

}
package com.dariusz.fakegpsdetector.db.dao

import androidx.room.*
import com.dariusz.fakegpsdetector.model.RoutersListModel

@Dao
interface RoutersListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insertAll(cellTowerList: List<RoutersListModel>)

    @Query("DELETE FROM routers_table")
    fun deleteAllRouters()

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT id, macAddress, frequency, level FROM routers_table ")
    fun getAllRouters(): List<RoutersListModel>

}
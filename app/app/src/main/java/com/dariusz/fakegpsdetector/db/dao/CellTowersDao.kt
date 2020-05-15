package com.dariusz.fakegpsdetector.db.dao

import androidx.room.*
import com.dariusz.fakegpsdetector.model.CellTowerModel

@Dao
interface CellTowersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insertAll(cellTowerList: List<CellTowerModel>)

    @Query("DELETE FROM celltowers_table")
    fun deleteAllCellTowers()

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT id, cellId, locationAreaCode, mobileCountryCode, mobileNetworkCode, signalStrength FROM celltowers_table ")
    fun getAllCellTowers(): List<CellTowerModel>

}
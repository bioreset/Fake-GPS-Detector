package com.dariusz.fakegpsdetector.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dariusz.fakegpsdetector.model.CellTowerModel

@Dao
interface CellTowersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insertAll(cellTowerList: List<CellTowerModel>)

    @Query("DELETE FROM celltowers_table")
    fun deleteAllCellTowers()

    @Query("SELECT id, cellId, locationAreaCode, mobileCountryCode, mobileNetworkCode, signalStrength FROM celltowers_table ")
    fun getAllCellTowers(): LiveData<List<CellTowerModel>>

}
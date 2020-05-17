package com.dariusz.fakegpsdetector.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dariusz.fakegpsdetector.model.LocationModel

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(locationModel: LocationModel)

    @Query("DELETE FROM location")
    fun deleteAllLocationInfo()

    @Query("SELECT * FROM location")
    fun getLocation(): LocationModel

}
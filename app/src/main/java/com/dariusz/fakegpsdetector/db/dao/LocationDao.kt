package com.dariusz.fakegpsdetector.db.dao

import androidx.room.*
import com.dariusz.fakegpsdetector.model.LocationModel

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(locationModel: LocationModel)

    @Query("DELETE FROM location")
    fun deleteAllLocationInfo()

    @Query("SELECT id, longitude, latitude FROM location WHERE id=1")
    fun getLocation(): List<LocationModel>

}
package com.dariusz.fakegpsdetector.db.dao

import androidx.lifecycle.LiveData
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

    @Query("SELECT id, longitude, latitude FROM location WHERE id=1")
    fun getLocation(): LiveData<LocationModel>

}
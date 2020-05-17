package com.dariusz.fakegpsdetector.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dariusz.fakegpsdetector.api.apimodel.ApiResponseModel

@Dao
interface LocationFromApiResponseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(apiResponse: ApiResponseModel)

    @Query("DELETE FROM locationfromapix_table")
    fun deleteAllLocationFromApiRecords()

    @Query("SELECT * FROM locationfromapix_table WHERE id=1")
    fun getLocationFromApiInfo(): ApiResponseModel

}
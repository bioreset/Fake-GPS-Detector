package com.dariusz.fakegpsdetector.db.dao

import androidx.room.*
import com.dariusz.fakegpsdetector.model.PhoneInfoModel

@Dao
interface PhoneInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(phoneInfoModel: PhoneInfoModel)

    @Query("DELETE FROM phoneinfo_table")
    fun deleteAllPhoneInfo()

    @Query("SELECT id, homeMobileCountryCode, homeMobileNetworkCode, radiotype, carrier FROM phoneinfo_table WHERE id=1")
    fun getPhoneInfo(): List<PhoneInfoModel>

}
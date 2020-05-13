package com.dariusz.fakegpsdetector.repository

import android.content.Context
import com.dariusz.fakegpsdetector.db.dao.PhoneInfoDao
import com.dariusz.fakegpsdetector.db.init.FGDDatabase
import com.dariusz.fakegpsdetector.model.PhoneInfoModel

class PhoneInfoRepository(context: Context) {

    private lateinit var phoneInfoDao: PhoneInfoDao

     fun insert(phoneinfo: PhoneInfoModel) {
        deleteAll()
        insertAsync(phoneinfo)
    }

     fun selectAll(): List<PhoneInfoModel> {
        return phoneInfoDao.getPhoneInfo()
    }

    private fun insertAsync(phoneinfo: PhoneInfoModel) {
        Thread(Runnable {
            phoneInfoDao.insert(phoneinfo)
        }).start()
    }

    private fun deleteAll() {
        phoneInfoDao.deleteAllPhoneInfo()
    }

    init {
        val db: FGDDatabase? = FGDDatabase.getInstanceOf(context)
        if (db != null) {
            phoneInfoDao = db.phoneInfoDao()
        }
    }

    companion object {
        @Volatile
        private var instance: PhoneInfoRepository? = null

        fun getInstance(context: Context): PhoneInfoRepository =
                instance ?: synchronized(this) {
                    instance ?: PhoneInfoRepository(context).also { instance = it }
                }
    }
}

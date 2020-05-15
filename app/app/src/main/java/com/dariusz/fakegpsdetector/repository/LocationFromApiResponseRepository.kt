package com.dariusz.fakegpsdetector.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.dariusz.fakegpsdetector.api.apimodel.ApiResponseModel
import com.dariusz.fakegpsdetector.db.dao.LocationFromApiResponseDao
import com.dariusz.fakegpsdetector.db.init.FGDDatabase

class LocationFromApiResponseRepository(context: Context) {

    private lateinit var locationFromApiResponse: LocationFromApiResponseDao

    fun insert(apiResponse: ApiResponseModel) {
        deleteAll()
        insertAsync(apiResponse)
    }

    fun selectAll(): LiveData<ApiResponseModel> {
        return locationFromApiResponse.getLocationFromApiInfo()
    }

    private fun insertAsync(apiResponse: ApiResponseModel) {
        Thread(Runnable {
            locationFromApiResponse.insert(apiResponse)
        }).start()
    }

    private fun deleteAll() {
        locationFromApiResponse.deleteAllLocationFromApiRecords()
    }

    init {
        val db: FGDDatabase? = FGDDatabase.getInstanceOf(context)
        if (db != null) {
            locationFromApiResponse = db.locationFromApiResponseDao()
        }
    }

    companion object {
        @Volatile
        private var instance: LocationFromApiResponseRepository? = null

        fun getInstance(context: Context): LocationFromApiResponseRepository =
                instance ?: synchronized(this) {
                    instance ?: LocationFromApiResponseRepository(context).also { instance = it }
                }
    }
}

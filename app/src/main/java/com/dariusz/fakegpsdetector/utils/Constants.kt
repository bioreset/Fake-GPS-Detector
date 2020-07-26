package com.dariusz.fakegpsdetector.utils

import com.dariusz.fakegpsdetector.R
import com.dariusz.fakegpsdetector.ui.MainActivity.Companion.mainContext
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
object Constants {

    // API SETUP
    const val API_URL = "https://www.googleapis.com/"
    const val API_HEADER = "Content-Type: application/json"
    const val API_POST = "geolocation/v1/geolocate"

    val API_KEY = mainContext?.getString(R.string.google_maps_api_key) ?: ""

    // ROOM SETUP
    const val DB_NAME = "fgd_database"
    const val DB_VER = 1
}

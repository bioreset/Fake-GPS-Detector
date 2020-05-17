package com.dariusz.fakegpsdetector.ui

import com.dariusz.fakegpsdetector.api.apimodel.ApiResponseModel

interface MainInterface {
    fun returnStatus(): String?
    fun doCheck(): ApiResponseModel
}
package com.dariusz.fakegpsdetector.utils

import com.dariusz.fakegpsdetector.model.ApiResponseModel
import com.dariusz.fakegpsdetector.utils.RepositoryUtils.performApiCall
import org.json.JSONObject

object ManageResponse {

    suspend fun manageResponse(response: String?, cacheCall: suspend (ApiResponseModel) -> Unit) =
        if (response != null) performApiCall(cacheCall.invoke(parseJSONResponse(response)))
        else null

    private fun parseJSONResponse(response: String): ApiResponseModel {
        return if (response.contains("location")) {
            val json = JSONObject(response)
            val jsonlocation = json.getJSONObject("location")
            val lat = jsonlocation.getDouble("lat")
            val lng = jsonlocation.getDouble("lng")
            val accuracy = json.getInt("accuracy")
            ApiResponseModel(
                status = "location",
                lng = lng,
                lat = lat,
                accuracy = accuracy
            )
        } else {
            ApiResponseModel(
                status = "location_error",
                lng = 0.0,
                lat = 0.00,
                accuracy = 0
            )
        }
    }

}

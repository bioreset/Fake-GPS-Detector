package com.dariusz.fakegpsdetector.utils

import android.util.Log
import com.dariusz.fakegpsdetector.model.ApiResponseModel
import org.json.JSONObject
import retrofit2.Response

object ManageResponse {

    private var result: ApiResponseModel = ApiResponseModel()

    fun translateResponse(response: Response<String>) {
        if (response.isSuccessful) {
            response.body()?.let {
                if (it.contains("location")) {
                    val json = JSONObject(response.body()!!)
                    val jsonlocation = json.getJSONObject("location")

                    val lat = jsonlocation.getDouble("lat")
                    val lng = jsonlocation.getDouble("lng")
                    val accuracy = json.getInt("accuracy")

                    result =
                        ApiResponseModel(
                            status = "location",
                            lng = lng,
                            lat = lat,
                            accuracy = accuracy
                        )

                    Log.d("API", "onResponse;  body: $result")

                } else {
                    Log.i("API-ERROR", "API ERROR ON RESPONSE")
                    result =
                        ApiResponseModel(
                            status = "location",
                            lng = 0.0,
                            lat = 0.00,
                            accuracy = 0
                        )
                }

            }
        }
    }

}
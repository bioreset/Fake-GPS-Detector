package com.dariusz.fakegpsdetector.api

import com.dariusz.fakegpsdetector.model.ApiResponseModel
import org.json.JSONObject

class ParseResponse {

    private var error_response_from_API: String = """{
                                 "error": {
                                  "errors": [
                                   {
                                    "domain": "global",
                                    "reason": "parseError",
                                    "message": "Parse Error",
                                   }
                                  ],
                                  "code": 400,
                                  "message": "Parse Error"
                                 }
                                }"""


    private var location_response_from_API: String = """{
                                          "location": {
                                            "lat": 33.3632256,
                                            "lng": -117.0874871
                                          },
                                          "accuracy": 20
                                        }"""


    private var status: String = ""

    private lateinit var result: List<ApiResponseModel>

    private fun stringToJSON(response: String): JSONObject {
        return JSONObject(response)
    }

    private fun parseJSON(response: JSONObject): List<ApiResponseModel> {
        val status_error: Boolean = response.has("error")
        val status_location: Boolean = response.has("location")
        when (status) {
            "error" -> {
                result = listOf(ApiResponseModel(
                        status = "error",
                        lat = 0.0,
                        long = 0.0
                ))
            }
            "location" -> {
                result = listOf(ApiResponseModel(
                        status = "location",
                        lat = response.getDouble("latitude"),
                        long = response.getDouble("longitude")
                ))
            }
            else -> {
                result = emptyList()
            }
        }
        return result

    }


}
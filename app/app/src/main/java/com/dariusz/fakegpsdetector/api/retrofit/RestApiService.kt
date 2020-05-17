package com.dariusz.fakegpsdetector.api.retrofit

import android.content.Context
import android.util.Log
import com.dariusz.fakegpsdetector.api.apimodel.ApiResponseModel
import com.dariusz.fakegpsdetector.repository.LocationFromApiResponseRepository
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestApiService(context: Context) {

    lateinit var result: ApiResponseModel

    var repo: LocationFromApiResponseRepository = LocationFromApiResponseRepository.getInstance(context)

    fun checkLocation(jsonRequest: String): ApiResponseModel {
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        result = ApiResponseModel()
        retrofit.checkLocation(jsonRequest, key = "AIzaSyC95vnmTsAOfnAWpQjcnXdElVss7Dnr5IY")
                .enqueue(
                        object : Callback<String> {
                            override fun onFailure(call: Call<String>, t: Throwable) {
                                Log.e("API-ERROR", "API LOCATION WITH ERROR")
                            }

                            override fun onResponse(call: Call<String>, response: Response<String>) {
                                if (response.isSuccessful) {
                                    response.body()?.let {
                                        if (it.contains("location")) {
                                            val json = JSONObject(response.body())
                                            val jsonlocation = json.getJSONObject("location")

                                            val lat = jsonlocation.getDouble("lat")
                                            val lng = jsonlocation.getDouble("lng")
                                            val accuracy = json.getInt("accuracy")

                                            result = ApiResponseModel(status = "location", lng = lng, lat = lat, accuracy = accuracy)

                                            Log.d("API", "onResponse;  body: $result")

                                            insertToDb(result)

                                            Log.d("API-DB", "Database added;  body: ${showFromDb()}")

                                        } else {
                                            Log.i("API-ERROR", "API ERROR ON RESPONSE")
                                            result = ApiResponseModel(status = "location", lng = 0.0, lat = 0.00, accuracy = 0)
                                        }
                                    }
                                }
                            }
                        }
                )
        return result
    }

    fun insertToDb(insert: ApiResponseModel) {
        return repo.insert(insert)
    }

    fun showFromDb(): ApiResponseModel {
        return repo.selectAll()
    }


}









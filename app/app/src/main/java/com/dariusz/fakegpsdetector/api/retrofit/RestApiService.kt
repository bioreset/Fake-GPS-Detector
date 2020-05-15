package com.dariusz.fakegpsdetector.api.retrofit

import android.content.Context
import android.util.Log
import com.dariusz.fakegpsdetector.api.apimodel.ApiResponseModel
import com.dariusz.fakegpsdetector.repository.LocationFromApiResponseRepository
import com.dariusz.fakegpsdetector.ui.MainActivity
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestApiService(private var context: Context) {

    lateinit var result: ApiResponseModel

    lateinit var send: MainActivity

    fun checkLocation(jsonRequest: String): ApiResponseModel {
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        result = ApiResponseModel()
        send = MainActivity()
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
                                            val long = jsonlocation.getDouble("lng")
                                            val accuracy = json.getInt("accuracy")

                                            result = ApiResponseModel(status = "location", long = long, lat = lat, accuracy = accuracy)

                                            Log.d("API", "onResponse;  body: $result")

                                            LocationFromApiResponseRepository.getInstance(context).insert(result)

                                        } else {
                                            Log.i("API-ERROR", "API ERROR ON RESPONSE")
                                            result = ApiResponseModel(status = "location", long = 0.0, lat = 0.00, accuracy = 0)
                                        }
                                    }
                                }
                            }
                        }
                )
        return result
    }

}









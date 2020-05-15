package com.dariusz.fakegpsdetector.api.retrofit

import android.content.Context
import android.util.Log
import com.dariusz.fakegpsdetector.api.apimodel.ApiResponseModel
import com.dariusz.fakegpsdetector.repository.LocationFromApiResponseRepository
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestApiService(private var context: Context) {

    lateinit var result : ApiResponseModel

    fun checkLocation(jsonRequest: String) {

        val retrofit = ServiceBuilder.buildService(RestApi::class.java)

        retrofit.checkLocation(jsonRequest).enqueue(

                object : Callback<ApiResponseModel> {

                    override fun onFailure(call: Call<ApiResponseModel>, t: Throwable) {
                        Log.e("API-ERROR", "API LOCATION WITH ERROR")
                    }

                    override fun onResponse(call: Call<ApiResponseModel>, response: Response<ApiResponseModel>) {
                        if (response.isSuccessful) {
                            if (response.raw().message.contains("location")) {

                                val json = JSONObject(response.toString())
                                val jsonlocation = json.getJSONObject("location")

                                val lat = jsonlocation.getDouble("lat")
                                val long = jsonlocation.getDouble("long")
                                val accuracy = jsonlocation.getInt("accuracy")

                               result = ApiResponseModel(status = "location",
                                        long = long, lat = lat, accuracy = accuracy)
                                Log.i("API-SUCCESS", "API RESPONSE: LAT: $lat, LONG: $long, ACCURACY: $accuracy")

                            } else {
                                result = ApiResponseModel(status = "error", long = 0.0, lat = 0.0, accuracy = 0)
                                Log.i("API-ERROR", "API ERROR ON RESPONSE")
                            }
                            LocationFromApiResponseRepository.getInstance(context).insert(result)
                        }
                    }
                }
        )

    }


}




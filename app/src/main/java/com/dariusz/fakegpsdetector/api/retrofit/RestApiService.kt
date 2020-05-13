package com.dariusz.fakegpsdetector.api.retrofit

import com.dariusz.fakegpsdetector.model.ApiResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestApiService {

    fun checkLocation(jsonRequest: String, onResult: (String?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.checkLocation(jsonRequest).enqueue(
                object : Callback<ApiResponseModel> {
                     override fun onFailure(call: Call<ApiResponseModel>, t: Throwable) {
                        onResult(null)
                    }

                    override fun onResponse(call: Call<ApiResponseModel>, response: Response<ApiResponseModel>) {
                        TODO("Not yet implemented")
                    }
                }
        )
    }
}

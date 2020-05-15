package com.dariusz.fakegpsdetector.api

import com.dariusz.fakegpsdetector.api.apimodel.ApiResponseModel

class ParseResponse {

    private var error_response_from_API: String = """{
                                 "error": {
                                  "errors": [
                                   {
                                    "domain": "global",
                                    "reason": "parseError",
                                    "message": "Parse Error"
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


}
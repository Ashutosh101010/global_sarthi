package com.global.appsarthi.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface AppApi {

    @Headers("Content-Type: application/json")
    @POST("customer/register-device")
    suspend fun postData(
        @Body body: Map<String, String>
    ) : Response<ApiResponse>

    @Headers("Content-Type: application/json")
    @POST("customer/update-location/{imei}")
    suspend fun postLocation(
        @Path("imei") otp: String,
        @Body body: Map<String, String>
    ) : Response<ApiResponse>
}
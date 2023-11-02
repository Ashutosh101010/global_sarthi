package com.global.appsarthi.network


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ApiResponse(
    @SerializedName("errorCode")
    val errorCode: Int,
    @SerializedName("errorDescription")
    val errorDescription: String,
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("retailerName")
    val retailerName: String,
    @SerializedName("retailerContact")
    val retailerContact: String,
)
package com.global.appsarthi.utils

import androidx.compose.runtime.Composable

interface UtilsInterface {
    @Composable
    fun setPermission() : Boolean
    fun getOs(): String
    fun getToken(): String
    fun getImei(callback:(String)-> Unit)
    fun getMac() : String
    fun getGsfId(): String?
    fun getLocation()
}
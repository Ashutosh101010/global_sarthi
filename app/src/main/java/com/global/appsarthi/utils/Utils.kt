package com.global.appsarthi.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.telephony.TelephonyManager
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.global.appsarthi.db.DataStoreManager
import com.global.appsarthi.network.ApiRepository
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Long
import javax.inject.Inject
import kotlin.Boolean
import kotlin.Exception
import kotlin.NumberFormatException
import kotlin.OptIn
import kotlin.String
import kotlin.TODO
import kotlin.Unit
import kotlin.arrayOf
import kotlin.let
import kotlin.toString


class Utils @Inject constructor(
    private val context: Context,
    private val telephonyManager: TelephonyManager?,
    private val scope: CoroutineScope?,
    private val dataStoreManager: DataStoreManager?,
    private val apiRepository: ApiRepository?
): UtilsInterface{

    @Composable
    @OptIn(ExperimentalPermissionsApi::class)
    override fun setPermission(): Boolean {
        val value = remember { mutableStateOf(false) }
        val multiplePermissionsState = rememberMultiplePermissionsState(
            listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE
            )
        )
        SideEffect {
            multiplePermissionsState.launchMultiplePermissionRequest()
        }

        if (multiplePermissionsState.allPermissionsGranted) {
            value.value = true
        } else {
            value.value = true ////Check it
        }
        return value.value
    }


    @SuppressLint("NewApi")
    override fun getImei(callback: (String) -> Unit) {
        scope?.launch {
            try {
                telephonyManager?.let { callback(it.imei) }
            }
            catch (e: Exception){
                delay(500)
                callback("Loading...")
//                callback("Loading...")
                getImei (callback)
            }
        }
    }


    @SuppressLint("HardwareIds")
    override fun getMac(): String {
        try {
                val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                val wifiInfo: WifiInfo? = wifiManager.connectionInfo
                return wifiInfo?.macAddress.toString()
        } catch (e: Exception) {
            e.localizedMessage?.let { Log.e("MAC_ERROR", it) }
        }
        return "Device not allow for MAC"
    }


    override fun getOs(): String {
        return Build.VERSION.RELEASE
    }

    override fun getToken(): String {
        TODO("Not yet implemented")
    }

    override fun getGsfId(): String? {
        val uri = Uri.parse("content://com.google.android.gsf.gservices")
        val ID_KEY = "android_id"
        val params = arrayOf(ID_KEY)
        val c = context.contentResolver.query(uri, null, null, params, null)
        if (c != null && (!c.moveToFirst() || c.columnCount < 2)) {
            if (!c.isClosed) c.close()
            return null
        }
        return try {
            if (c != null) {
                val result = Long.toHexString(c.getString(1).toLong())
                if (!c.isClosed) c.close()
                result
            } else {
                null
            }
        } catch (e: NumberFormatException) {
            if (!c!!.isClosed) c.close()
            null
        }
    }

    @SuppressLint("MissingPermission")
    override fun getLocation() {
        val value = mutableStateOf(false)
        val locationClient = LocationServices.getFusedLocationProviderClient(context)
        locationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            CancellationTokenSource().token
        ).addOnSuccessListener { location ->
            try {
                Log.i(
                    "LOCATION",
                    "Lat : ${location.latitude} and \nlong : ${location.longitude}"
                )
            } catch (e: Exception) {
                e.localizedMessage?.let { Log.i("LOCATION_ERROR", it) }
            }
        }.addOnFailureListener {
            it.localizedMessage?.let { it1 -> Log.i("LOCATION_FAIL", it1) }
        }
    }

}
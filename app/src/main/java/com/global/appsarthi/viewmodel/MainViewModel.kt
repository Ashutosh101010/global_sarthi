package com.global.appsarthi.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.global.appsarthi.db.DataStoreManager
import com.global.appsarthi.network.ApiRepository
import com.global.appsarthi.utils.Utils
import com.global.sarthi.worker.WorkerCaller
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val context: Context,
    private val apiRepository: ApiRepository,
    private val utils: Utils,
    private val dataStoreManager: DataStoreManager
): ViewModel() {
    var apiSuccessState : Flow<Boolean> = flowOf()
    val fcmToken = mutableStateOf("")
    val imei = mutableStateOf("")
    val gsfId = mutableStateOf("")
    val mac = mutableStateOf(utils.getMac())
    init {
        WorkerCaller.callLocationWorker(context)
        utils.getImei {
            imei.value = it
        }
        viewModelScope.launch {
            launch{
                dataStoreManager.getToken().collect{
                    fcmToken.value = it
                    gsfId.value = utils.getGsfId()!!
                    Log.e("VIEWMODEL", "VIEWMODEL")
                    Log.e("VIEWMODEL_GSF.ID", gsfId.value)
                    Log.e("VIEWMODEL_MAC", mac.value)
                }
            }
            launch{
//                try{
//                    val requestBody: MutableMap<String, String> = HashMap()
//                    requestBody["imei"] = "1234567891245681"
//                    requestBody["deviceId"] = gsfId.value
//                    requestBody["mac"] = mac.value
//                    requestBody["fcmToken"] = fcmToken.value
//                    delay(5000)
//                    apiSuccessState = flowOf(apiRepository.postData(requestBody))
//                }catch (e: Exception){
//                    e.localizedMessage?.let { Log.e("API_ERROR", it) }
//                }
            }
        }
    }
}
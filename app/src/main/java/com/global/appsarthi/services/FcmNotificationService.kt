package com.global.appsarthi.services

import android.util.Log
import com.global.appsarthi.db.DataStoreManager
import com.global.appsarthi.viewmodel.MainViewModel
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FcmNotificationService : FirebaseMessagingService() {

    @Inject
    lateinit var dataStoreManager: DataStoreManager
    @Inject
    lateinit var scope: CoroutineScope
    @Inject
    lateinit var viewModel: MainViewModel

    override fun onNewToken(token: String) {
        super.onNewToken(token)
       Log.e("TOKEN",token)
        viewModel.fcmToken.value = token
        scope.launch {
            dataStoreManager.saveToken(token)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val command: String = message.data["COMMAND"].toString()
        when(command.uppercase()){
            "LOCATION"->{

            }
        }
    }
}
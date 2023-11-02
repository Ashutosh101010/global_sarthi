package com.global.sarthi.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.global.appsarthi.network.ApiRepository
import com.global.appsarthi.network.AppApi
import com.global.appsarthi.utils.Utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LocationWorker (
    context: Context,
    workerParameters: WorkerParameters,
) : Worker(context, workerParameters) {
    override fun doWork(): Result {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://110.227.200.246:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService = retrofit.create(AppApi::class.java)
        val scope = CoroutineScope(Dispatchers.IO)
        val apiRepository = ApiRepository(applicationContext, appApi = apiService)
        val utils = Utils(
            context = applicationContext,
            telephonyManager = null,
            dataStoreManager = null,
            scope = scope,
            apiRepository = apiRepository
            )


        Log.e("LOCATION_WORKER_CALLED","LOCATION_WORKER_CALLED")
        utils.getLocation()
        return Result.success()
    }
}
package com.global.appsarthi.db

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject

const val APP_DATASTORE = "app_datastore"
private val Context.datastore : DataStore<Preferences> by preferencesDataStore(name = APP_DATASTORE)
class DataStoreManager @Inject constructor(@ApplicationContext private val context: Context) {

    private val SAVE_TOKEN = stringPreferencesKey("SAVE_TOKEN")
    private val SAVE_REGISTER_STATE = booleanPreferencesKey("SAVE_REGISTER_STATE")
    private val SAVE_FIRST_LAUNCH_STATE = booleanPreferencesKey("SAVE_FIRST_LAUNCH_STATE")

    suspend fun saveToken(token:String){
        context.datastore.edit {
            it[SAVE_TOKEN] = token
        }
    }

    suspend fun saveRegisterState(state: Boolean){
        context.datastore.edit {
            it[SAVE_REGISTER_STATE] = state
        }
    }

    suspend fun saveFirstLaunchState(state: Boolean){
        context.datastore.edit {
            it[SAVE_FIRST_LAUNCH_STATE] = state
        }
    }

    fun getToken() = context.datastore.data.map {
        it[SAVE_TOKEN]?:""
    }

    fun getRegisterState() = context.datastore.data.map {
        it[SAVE_REGISTER_STATE]?:false
    }

    fun getFirstLaunchState() = context.datastore.data.map {
        it[SAVE_FIRST_LAUNCH_STATE]?:true
    }
}
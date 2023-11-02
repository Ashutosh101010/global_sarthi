package com.global.appsarthi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.SettingsEthernet
import androidx.compose.material.icons.filled.Token
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.global.appsarthi.db.DataStoreManager
import com.global.appsarthi.network.ApiRepository
import com.global.appsarthi.ui.theme.GlobalSarthiTheme
import com.global.appsarthi.utils.Utils
import com.global.appsarthi.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var utils: Utils

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    @Inject
    lateinit var apiRepository: ApiRepository

    @Inject
    lateinit var scope: CoroutineScope

    @Inject
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            LaunchedEffect(Unit) {
//                val requestBody: MutableMap<String, String> = HashMap()
//                requestBody["lat"] = "121212.12121"
//                requestBody["lon"] = "121212.1212121"
//                apiRepository.postLocation("1234567891245681",requestBody)
//            }
            val dataStoreManager = DataStoreManager(this)
            GlobalSarthiTheme {
                val registerState = remember {
                    mutableStateOf(false)
                }
                LaunchedEffect(Unit) {
                    dataStoreManager.getRegisterState().collect{
                        registerState.value = it
                    }
                }
//                val registerState = dataStoreManager.getRegisterState().collectAsState(false)
//                val firstLaunchState = dataStoreManager.getFirstLaunchState().collectAsState(true)
                // A surface container using the 'background' color from the theme
                utils.setPermission()
                if (registerState.value) {
                        val i = Intent(this, LockActivity::class.java)
                        startActivity(i)
                        finish()
                } else {
                    RegisterScreen()
                }

            }
        }
    }

    @Composable
     fun RegisterScreen() {
        var color by remember { mutableStateOf(Color(0xFFF1F4FC)) }
        var myText by remember { mutableStateOf("in progress...") }
        var imeiResponse by remember { mutableStateOf("") }
        var myProgress by remember { mutableStateOf(true) }
        var apiResponse by remember { mutableStateOf(false) }
        var timeout by remember { mutableStateOf(false) }
        window.statusBarColor = color.toArgb()
        window.navigationBarColor = color.toArgb()
        Log.e("RESSS", apiResponse.toString())
        LaunchedEffect(Unit) {
            utils.getImei {
                launch {
                    if (it != "Loading...") {
                        System.out.println("loading");
                        try {
                            delay(4000)
                            if(viewModel.fcmToken.value.isNotEmpty()){
                                System.out.println("is not empty");
                                val requestBody: MutableMap<String, String> = HashMap()
                                requestBody["imei"] = viewModel.imei.value
                                requestBody["deviceId"] = viewModel.gsfId.value
                                requestBody["mac"] = viewModel.mac.value
                                requestBody["fcmToken"] = viewModel.fcmToken.value
                                delay(1000)
                                apiResponse = apiRepository.postData(requestBody,applicationContext)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace();
                            e.localizedMessage?.let { Log.e("API_ERROR", it) }
                        }
                    }else{
                        System.out.println("else");
                    }
                }
            }
            delay(60000)
            color = Color(0xFFFFEB3B)
            myText = "TimeOut!"
            myProgress = false
            timeout = true
        }
        if (apiResponse) {
            color = Color(0xFF00FF73)
            myText = "Successful"
            myProgress = false
            LaunchedEffect(Unit) {
                delay(2000)
                dataStoreManager.saveRegisterState(true)
                setResult(RESULT_OK)
                finish();
            }
        }
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(color)
        ) {
            val (retryIcon, data, loader, info) = createRefs()

            Column(modifier = Modifier.constrainAs(info) {
                top.linkTo(parent.top, 72.dp)
                start.linkTo(parent.start, 35.dp)
            }) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()){
                    Text(
                        text = "Enrolment",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                    )
                    if (timeout){
                        Icon(
                            Icons.Default.Replay, "",
                            modifier = Modifier
                                .padding(start = 30.dp)
                                .size(40.dp)
                                .clickable {
                                    val intent = intent
                                    setResult(RESULT_OK);
                                    finish()
                                    startActivity(intent)
                                }
                        )
                    }
                }
                Text(
                    text = myText,
                    fontSize = 20.sp,
                    color = Color.Black,
                )
                Spacer(modifier = Modifier.padding(bottom = 12.dp))
            }

            if (myProgress) {
                LinearProgressIndicator(trackColor = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .constrainAs(loader) {
                            top.linkTo(info.bottom)
                            start.linkTo(parent.start, 35.dp)
                        })
            }

            DataCloumn(gsfId = viewModel.gsfId.value,
                modifier = Modifier
                    .padding(top = 5.dp, start = 35.dp, end = 35.dp)
                    .constrainAs(data) {
                        top.linkTo(info.bottom)
                        bottom.linkTo(parent.bottom)
                    })

        }
    }

    @Composable
    fun DataCloumn(modifier: Modifier, gsfId: String) {
        Column(modifier = modifier) {
            ModChip(text = "IMEI : ", viewModel.imei.value, Icons.Default.Info)
            ModChip(text = "GSF Id : ", viewModel.gsfId.value, Icons.Default.CloudUpload)
            ModChip(text = "MAC : ", viewModel.mac.value, Icons.Default.SettingsEthernet)
            ModChip(
                text = "Token : ", text2 = viewModel.fcmToken.value, Icons.Default.Token
            )
        }
    }

    @Composable
    fun ModChip(text: String, text2: String, icon: ImageVector) {
        Row(Modifier.padding(5.dp)) {
            Icon(
                imageVector = icon,
                contentDescription = "",
                tint = Color.Black,
                modifier = Modifier
                    .padding(5.dp)
                    .size(18.dp)
            )
            Column {
                Text(
                    text = text,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = text2,
                    color = Color.Black,
                    fontSize = 15.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp)
                )
            }
        }
    }

}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GlobalSarthiTheme {
//        RegisterScreen()
    }
}
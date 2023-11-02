package com.global.appsarthi.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.global.appsarthi.MainActivity
import com.global.appsarthi.db.DataStoreManager
import kotlinx.coroutines.delay

@Composable
fun RegisterScreen(activity: MainActivity, dataStoreManager: DataStoreManager, gsfId:String) {
    var color by remember{ mutableStateOf(Color(0xff004fb9)) }
    var myText by remember{ mutableStateOf("in progress...") }
    var myProgress by remember{ mutableStateOf(true) }
    LaunchedEffect(Unit){
        delay(5000)
//        color = Color(0xff02b033)
        color = Color(0xFF00FF73)
        myText = "Success"
        myProgress = false
        delay(2000)
//        dataStoreManager.saveRegisterState(true)
    }
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
    ) {
        val (icon, data, loader, info) = createRefs()
        Column(modifier = Modifier.constrainAs(info) {
            top.linkTo(parent.top, 72.dp)
            start.linkTo(parent.start, 35.dp)
        }) {
            Text(
                text = "Enrolment",
                fontSize = 40.sp,
                color = MaterialTheme.colorScheme.onPrimary,
            )
            Text(
                text = myText,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }

        if (myProgress){
            LinearProgressIndicator(trackColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .constrainAs(loader) {
                        top.linkTo(info.bottom)
                        start.linkTo(parent.start, 35.dp)

                    })
        }

        DataCloumn(
            gsfId = gsfId,
            modifier = Modifier
                .padding(35.dp)
                .constrainAs(data) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                })

    }
}

@Composable
fun DataCloumn(modifier: Modifier, gsfId: String) {
    Column(modifier = modifier) {
        ModChip(text = "IMEI : ", "***************", Icons.Default.Info)
        ModChip(text = "GSF Id : ", gsfId, Icons.Default.CloudUpload)
        ModChip(text = "MAC : ", "12.10.21.1110", Icons.Default.SettingsEthernet)
        ModChip(
            text = "Token : ",
            "c8f6e43FpKc:APA91bHf8oCnOcYdL6sHuX6DhvUtJTlgh_v3GcE9YcGMvN85vwWJQb6IcLj6dc8_x9MsGKprEwAMO5TlRxmZR3yR7C2jnVZcO5UqRczG3YvCKe7M2gXQ1F_GUG0mPVWKC1GqZNWcBS0px3",
            Icons.Default.Token
        )
    }
}

@Composable
fun ModChip(text: String, text2: String, icon: ImageVector) {
    Row(Modifier.padding(5.dp)) {
        Icon(
            imageVector = icon,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .padding(5.dp)
                .size(22.dp)
        )
        Column {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 22.sp
            )
            Text(
                text = text2,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp)
            )
        }
    }
}


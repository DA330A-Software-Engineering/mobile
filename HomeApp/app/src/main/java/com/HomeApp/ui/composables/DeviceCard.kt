package com.HomeApp.ui.composables

import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Curtains
import androidx.compose.material.icons.filled.DoorFront
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.HomeApp.R
import com.HomeApp.screens.Devices
import com.HomeApp.util.ApiConnector
import com.HomeApp.util.ApiResult
import com.HomeApp.util.HttpStatus
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

@Composable
fun DeviceCard(
    navController: NavController,
    modifier: Modifier = Modifier,
    deviceItem: DocumentSnapshot
) {
    val item = deviceItem
    val state = deviceItem.get("state") as Map<*, *>
    val coroutine = rememberCoroutineScope()

    val cardIcon: ImageVector = when (deviceItem.get("type")) {
        "light" -> Icons.Filled.Lightbulb
        "door" -> Icons.Filled.DoorFront
        "curtain" -> Icons.Filled.Curtains
        else -> Icons.Filled.BrokenImage
    }

    val deviceState: String = when (deviceItem.get("type")) {
        "toggle" -> if (state["on"] == "true") "On" else "Off"
        "door" -> if (state["open"] == true) "Open" else "Closed"
        "curtain" -> if (state["open"] == "true") "Open" else "Open"
        else -> {
            "No State"
        }
    }

    Button(
        onClick = { changeState(id = deviceItem.id, state= deviceItem.get("state") as Map<*, *>, type = deviceItem.get("type") as String, coroutine= coroutine)},
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.LightSteelBlue)),
        shape = RoundedCornerShape(10)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 3.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Icon(
                    imageVector = cardIcon,
                    contentDescription = deviceItem.get("type") as String?,
                    modifier = Modifier
                        .size(70.dp)
                        .padding(top = 7.dp)
                )
                Spacer(modifier = Modifier.width(7.dp))
                Text(
                    text = deviceItem.get("name") as String,
                    fontSize = 25.sp,
                    modifier = Modifier
                        .fillMaxHeight()
                        .wrapContentHeight(align = Alignment.CenterVertically),
                    style = TextStyle(textDecoration = TextDecoration.Underline)
                )
            }
            Text(
                text = deviceState,
                fontSize = 18.sp,
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .wrapContentHeight(align = Alignment.CenterVertically)
                    .padding(end = 7.dp),
                textAlign = TextAlign.Right,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}


private fun changeState(id: String, state: Map<*, *>, type:String, coroutine:CoroutineScope){

    val onFetchTasks: (ApiResult) -> Unit = {
        val data: JSONObject = it.data()
        val msg: String = data.get("msg") as String
        when (it.status()) {
            HttpStatus.SUCCESS -> {
            }
            HttpStatus.UNAUTHORIZED -> {

            }
            HttpStatus.FAILED -> {

            }
        }
    }
    coroutine.launch(Dispatchers.IO) {
        ApiConnector.action(
            id = id,
            state = state,
            type = type,
            onRespond = onFetchTasks

        )
    }
}


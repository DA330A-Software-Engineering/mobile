package com.HomeApp.ui.composables

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.HomeApp.ui.theme.RaminGrey
import com.HomeApp.util.ApiConnector
import com.HomeApp.util.ApiResult
import com.HomeApp.util.HttpStatus
import com.HomeApp.util.LocalStorage
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

@Composable
fun DeviceCard(
    navController: NavController,
    modifier: Modifier = Modifier,
    deviceItem: DocumentSnapshot
) {
    val context: Context = LocalContext.current
    val item = deviceItem
    val state = deviceItem.get("state") as Map<String, Boolean>
    val coroutine = rememberCoroutineScope()

    val cardIcon: ImageVector = when (deviceItem.get("type")) {
        "toggle" -> Icons.Filled.Lightbulb
        "door" -> Icons.Filled.DoorFront
        "window" -> Icons.Outlined.Window
        "screen" -> Icons.Outlined.SmartScreen
        "buzzer" -> Icons.Outlined.SurroundSound
        else -> Icons.Filled.BrokenImage
    }

    val deviceState: String = when (deviceItem.get("type")) {
        "toggle", "fan", "screen" -> if (state["on"] == true) "On" else "Off"
        "door", "window" -> if (state["open"] == true) "Open" else "Closed"
        else -> {
            "No State"
        }
    }

    val actionIcon: ImageVector? = when (deviceItem.get("type")) {
        "door", "window" -> if (state["locked"] == true) Icons.Outlined.Lock else Icons.Outlined.LockOpen
        "fan" -> Icons.Outlined.CompareArrows
        else -> null

    }

    Row(modifier = Modifier.height(60.dp)) {
        Button(
            onClick = {
                changeState(
                    context = context,
                    id = deviceItem.id,
                    state = deviceItem.get("state") as Map<String, Boolean>,
                    type = deviceItem.get("type") as String,
                    coroutine = coroutine,
                    changedState = if (deviceItem.get("type") == "door" ||  deviceItem.get("type") == "window") "locked" else "reverse"
                )
            },
            modifier = modifier.then(
                Modifier
                    .border(
                        width = 1.dp,
                        shape = RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp),
                        color = RaminGrey
                    )
                    .fillMaxHeight()
                    .weight(1f)
            ),
            colors = ButtonDefaults.buttonColors(backgroundColor = RaminGrey),
            elevation = null
        ) {
            if (actionIcon != null) {
                Icon(
                    imageVector = actionIcon,
                    contentDescription = "locked",
                    modifier = Modifier.size(48.dp)
                )
            }
        }
        Button(
            onClick = {
                changeState(
                    context = context,
                    id = deviceItem.id,
                    state = deviceItem.get("state") as Map<String, Boolean>,
                    type = deviceItem.get("type") as String,
                    coroutine = coroutine
                )
            },
            modifier = modifier.then(
                Modifier
                    .border(
                        width = 1.dp,
                        shape = RoundedCornerShape(topEnd = 5.dp, bottomEnd = 5.dp),
                        color = RaminGrey
                    )
                    .fillMaxHeight()
                    .weight(5f)
            ),
            elevation = null,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 3.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = cardIcon,
                        contentDescription = deviceItem.get("type") as String?,
                        modifier = Modifier
                            .size(48.dp)
                    )
                    Spacer(modifier = Modifier.width(7.dp))
                    Text(
                        text = deviceItem.get("name") as String,
                        fontSize = 25.sp,
                        modifier = Modifier
                            .fillMaxHeight()
                            .wrapContentHeight(align = Alignment.CenterVertically)
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
}


private fun changeState(
    context: Context,
    id: String,
    state: Map<String, Boolean>,
    type: String,
    coroutine: CoroutineScope,
    changedState: String? = null
) {
    val updateState = JSONObject()
    if (type == "toggle") {
        //updateState = mutableMapOf("on" to !state["on"]!!)
        updateState.put("on", !state["on"]!!)
    }
    else if (type == "door" || type == "window") {
        if (changedState == "locked") {
            updateState.put("locked", !state["locked"]!!)
        } else {
            updateState.put("open", !state["open"]!!)
        }

        //updateState = mutableMapOf("locked" to state["locked"] as Boolean, "open" to !state["open"]!!
    }else if (type == "fan") {
        if (changedState == "reverse") {
            updateState.put("reverse", !state["reverse"]!!)
        } else {
            updateState.put("on", !state["on"]!!)
        }
    }
    val changeDeviceState: (ApiResult) -> Unit = {
        //val data: JSONObject = it.data()
//        val msg: String = data.get("msg") as String
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
        ApiConnector.deviceAction(
            token = LocalStorage.getToken(context),
            id = id,
            state = updateState,
            type = type,
            onRespond = changeDeviceState
        )
    }
}

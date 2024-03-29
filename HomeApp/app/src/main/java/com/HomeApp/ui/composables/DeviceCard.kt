package com.HomeApp.ui.composables

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.DoorFront
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.outlined.CompareArrows
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material.icons.outlined.Sensors
import androidx.compose.material.icons.outlined.SmartScreen
import androidx.compose.material.icons.outlined.SurroundSound
import androidx.compose.material.icons.outlined.TextIncrease
import androidx.compose.material.icons.outlined.Window
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.HomeApp.screens.SelectedItems
import com.HomeApp.ui.navigation.Triggers
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
    val state = deviceItem.get("state") as Map<*, *>
    val coroutine = rememberCoroutineScope()
    var editDialog by remember { mutableStateOf(false) }

    val cardIcon: ImageVector = when (deviceItem.get("type")) {
        "toggle" -> Icons.Filled.Lightbulb
        "openLock" -> if (deviceItem.get("tag") == "window") Icons.Outlined.Window else Icons.Filled.DoorFront
        "screen" -> Icons.Outlined.SmartScreen
        "buzzer" -> Icons.Outlined.SurroundSound
        "sensor" -> Icons.Outlined.Sensors
        "fan" -> Icons.Outlined.RestartAlt
        else -> Icons.Filled.BrokenImage
    }
    val deviceState: String = when (deviceItem.get("type")) {
        "toggle", "fan", "screen" -> if (state["on"] == true) "On" else "Off"
        "openLock" -> if (state["open"] == true) "Open" else "Close"
        else -> {
            ""
        }
    }
    val actionIcon: ImageVector? = when (deviceItem.get("type")) {
        "openLock" -> if (state["locked"] == true) Icons.Outlined.Lock else Icons.Outlined.LockOpen
        "fan" -> Icons.Outlined.CompareArrows
        "screen" -> Icons.Outlined.TextIncrease
        "buzzer" -> Icons.Outlined.MusicNote
        else -> null
    }
    val stateList = listOf("fan", "openLock", "toggle")
    var button1type = ""
    if (editDialog) {
        AlertDialog(
            onDismissRequest = { editDialog = false },
            title = { Text(deviceItem.get("name") as String) },
            text = {
                EditDeviceState(
                    deviceItem = deviceItem,
                    type = deviceItem.get("type") as String,
                    onDelEdit = { newState -> editDialog = newState })
            },
            confirmButton = {
            }
        )
    }

    Row(modifier = Modifier.height(45.dp)) {
        Button(
            onClick = {
                when (deviceItem.get("type") as String) {
                    "sensor" -> {
                        SelectedItems.setSensorId(deviceItem.id)
                        navController.navigate(Triggers.route)
                    }
                    in stateList -> {
                        changeState(
                            context = context,
                            id = deviceItem.id,
                            state = deviceItem.get("state") as Map<String, Boolean>,
                            type = deviceItem.get("type") as String,
                            coroutine = coroutine,
                            changedState = if (deviceItem.get("type") == "openLock") "locked" else "reverse"
                        )
                    }
                    "sensor" -> {}
                    else -> {
                        editDialog = true
                    }
                }
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
                if (deviceItem.get("type") as String == "sensor") {
                    SelectedItems.setSensorId(deviceItem.id)
                    navController.navigate(Triggers.route)
                } else {
                    changeState(
                        context = context,
                        id = deviceItem.id,
                        state = deviceItem.get("state") as Map<String, Boolean>,
                        type = deviceItem.get("type") as String,
                        coroutine = coroutine
                    )
                }
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
                    .padding(end = 3.dp),
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
                        fontSize = 21.sp,
                        modifier = Modifier
                            .fillMaxHeight()
                            .wrapContentHeight(align = Alignment.CenterVertically)
                    )
                }
                Text(
                    text = deviceState,
                    fontSize = 14.sp,
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
    if (type == "toggle" || type == "screen") {
        //updateState = mutableMapOf("on" to !state["on"]!!)
        updateState.put("on", !state["on"]!!)
    } else if (type == "openLock") {
        if (changedState == "locked") {
            updateState.put("locked", !state["locked"]!!)
        } else {
            updateState.put("open", !state["open"]!!)
        }

        //updateState = mutableMapOf("locked" to state["locked"] as Boolean, "open" to !state["open"]!!
    } else if (type == "fan") {
        if (changedState == "reverse") {
            updateState.put("reverse", !state["reverse"]!!)
        } else {
            updateState.put("on", !state["on"]!!)
        }
    }
    val changeDeviceState: (ApiResult) -> Unit = {
        when (it.status()) {
            HttpStatus.SUCCESS -> {

            }
            HttpStatus.UNAUTHORIZED -> {

            }
            HttpStatus.FAILED -> {

            }
            else -> {}
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

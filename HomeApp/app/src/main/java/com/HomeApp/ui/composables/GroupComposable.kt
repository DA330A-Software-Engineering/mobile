package com.HomeApp.ui.composables

import android.content.Context
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.HomeApp.ui.theme.RaminGrey
import com.HomeApp.util.*
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject


data class GroupsClass(
    var id: String = "",
    var name: String = "",
    var description: String = "",
    //var state: String = "",
    var devices: List<String> = emptyList()
)


@Composable
fun GroupComposable(
    modifier: Modifier = Modifier,
    groupItem: DocumentSnapshot,
    //groupState: String
) {
    var editGroup by remember { mutableStateOf(false) }
    val deviceList = groupItem.get("devices") as ArrayList<String>
    val state: Map<String, Any> = emptyMap()
    var groupStateBool by remember {
        mutableStateOf(true)
    }
    var groupType by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current
    val coroutine = rememberCoroutineScope()
    LaunchedEffect(context) {
        for (device in deviceList) {
            getDocument("devices", device) { doc ->
                if (doc != null) {
                    groupType = doc.get("type") as String
                    val deviceState = doc.get("state") as Map<*, *>
                    if (groupType == "openLock" && !(deviceState["open"] as Boolean)) {
                        groupStateBool = false
                        groupType = "openLock"
                    } else if (groupType == "toggle" && !(deviceState["on"] as Boolean)) {
                        groupStateBool = false
                        groupType = "toggle"
                    } else if (groupType == "fan" && !(deviceState["on"] as Boolean)) {
                        groupStateBool = false
                        groupType = "toggle"
                    }

                }
            }
        }
    }

    var groupState by remember {
        mutableStateOf("")
    }
    groupState = when (groupType) {
        "openLock" -> if (groupStateBool) "Open" else "Closed"
        "toggle" -> if (groupStateBool) "On" else "Off"
        else -> {
            ""
        }
    }

    Button(
        onClick = {
            coroutine.launch(Dispatchers.IO) {
                changeGroupState(
                    context = context,
                    newState = !groupStateBool,
                    deviceList = deviceList,
                    groupType = groupType
                )
                groupStateBool = !groupStateBool
            }


        }, modifier = Modifier
            .border(
                width = 1.dp,
                shape = RoundedCornerShape(10.dp),
                color = RaminGrey
            )
            .width(130.dp)
            .height(110.dp),
        contentPadding = PaddingValues(start = 0.dp, top = 8.dp, end = 0.dp, bottom = 0.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 15.dp)) {
            Text(
                text = groupItem.get("name") as String,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                textDecoration = TextDecoration.Underline,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.weight(0.2f))
            Row(modifier = Modifier.weight(0.5f)) {
                Text(text = groupState, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = {
                        editGroup = true

                    }, modifier = Modifier
                        .size(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Settings, contentDescription = "Settings Button",
                        modifier = Modifier.scale(1f),
                        tint = Color.White
                    )
                }
            }

        }

        if (editGroup) {
            AlertDialog(
                onDismissRequest = { editGroup = false },
                title = { Text(groupItem.get("name") as String) },
                text = {
                    EditGroup(
                        groupItem = groupItem,
                        onDelEdit = { newState -> editGroup = newState })
                },
                confirmButton = {}
            )
        }

    }
}

private fun changeGroupState(
    context: Context,
    newState: Boolean,
    deviceList: ArrayList<String>,
    groupType: String
) {
    val onUpdateState: (ApiResult) -> Unit = {
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

    if (deviceList.size != 0) {
        for (device in deviceList) {
            val updateState = JSONObject()
            if (groupType == "toggle" || groupType == "fan") {
                //updateState = mutableMapOf("on" to !state["on"]!!)
                updateState.put("on", newState)
            } else if (groupType == "openLock") {
                updateState.put("open", newState)

                //updateState = mutableMapOf("locked" to state["locked"] as Boolean, "open" to !state["open"]!!
            }
            Log.d("LOOKIE HERE", updateState.toString())
            ApiConnector.deviceAction(
                token = LocalStorage.getToken(context),
                id = device,
                type = groupType,
                onRespond = onUpdateState,
                state = updateState

            )
        }
    }


}



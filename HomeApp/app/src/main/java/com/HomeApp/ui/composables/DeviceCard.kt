package com.HomeApp.ui.composables

import androidx.compose.foundation.border
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
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.HomeApp.ui.theme.RaminGrey
import com.HomeApp.util.ApiConnector
import com.HomeApp.util.ApiResult
import com.HomeApp.util.HttpStatus
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
    val item = deviceItem
    val state = deviceItem.get("state") as Map<String, Boolean>
    val coroutine = rememberCoroutineScope()

    val cardIcon: ImageVector = when (deviceItem.get("type")) {
        "toggle" -> Icons.Filled.Lightbulb
        "door" -> Icons.Filled.DoorFront
        "curtain" -> Icons.Filled.Curtains
        else -> Icons.Filled.BrokenImage
    }

    val deviceState: String = when (deviceItem.get("type")) {
        "toggle" -> if (state["on"] == true) "On" else "Off"
        "door" -> if (state["open"] == true) "Open" else "Closed"
        "curtain" -> if (state["open"] == true) "Open" else "Closed"
        else -> {
            "No State"
        }
    }

    val actionIcon: ImageVector? = when (deviceItem.get("type")) {
        "door" -> if (state["locked"] == true) Icons.Outlined.Lock else Icons.Outlined.LockOpen
        else -> null

    }

    Row(modifier = Modifier.height(60.dp)) {
        Button(
            onClick = {
                changeState(
                    id = deviceItem.id,
                    state = deviceItem.get("state") as Map<String, Boolean>,
                    type = deviceItem.get("type") as String,
                    coroutine = coroutine,
                    changedState = "locked"
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

//    Button(
//        onClick = {
//
//        },
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(80.dp),
//        contentPadding = PaddingValues(0.dp),
//        colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.LightSteelBlue)),
//        shape = RoundedCornerShape(10)
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 3.dp),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//
//            Row {
//                Icon(
//                    imageVector = cardIcon,
//                    contentDescription = deviceItem.get("type") as String?,
//                    modifier = Modifier
//                        .size(70.dp)
//                        .padding(top = 7.dp)
//                )
//                Spacer(modifier = Modifier.width(7.dp))
//                Text(
//                    text = deviceItem.get("name") as String,
//                    fontSize = 25.sp,
//                    modifier = Modifier
//                        .fillMaxHeight()
//                        .wrapContentHeight(align = Alignment.CenterVertically),
//                    style = TextStyle(textDecoration = TextDecoration.Underline)
//                )
//            }
//            Text(
//                text = deviceState,
//                fontSize = 18.sp,
//                modifier = Modifier
//                    .fillMaxHeight()
//                    .fillMaxWidth()
//                    .wrapContentHeight(align = Alignment.CenterVertically)
//                    .padding(end = 7.dp),
//                textAlign = TextAlign.Right,
//                fontWeight = FontWeight.Bold,
//            )
//        }
//    }
}


private fun changeState(
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
    } else if (type == "door") {
        if (changedState == "locked") {
            updateState.put("locked", !state["locked"]!!)
        } else {
            updateState.put("open", !state["open"]!!)
        }
        //updateState = mutableMapOf("locked" to state["locked"] as Boolean, "open" to !state["open"]!!)

//
    }
    //Log.d("I am trying", updateState.toString())
    //val newState = Json.encodeToString(updateState)
    //val newState = Gson().toJson(updateState)


    //Log.d(TAG, "new state $newState")
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
            id = id,
            state = updateState,
            type = type,
            onRespond = changeDeviceState
        )
        /**
        Log.d("LOOK HERE", id)
        Log.d("LOOK HERE", newState)
        Log.d("LOOK HERE", type)
        val client = OkHttpClient()

        val formBody: RequestBody = FormBody.Builder()
        .add("id", id)
        .add("state", newState)
        .add("type", type)
        .build()



        val request: Request = Request.Builder()
        //            .header(AUTH_TOKEN_NAME, token)
        .header("Content-Type", "application/json")
        .url("http://10.0.2.2:3000/devices/actions")
        .put(formBody)
        .build()
        val response = client.newCall(request).execute()
        val responseCode = response.code
        val responseBody = response.body?.string()
        Log.d("----------RESPONSE CODE", responseCode.toString())
        Log.d("----------RESPONSE BODY", responseBody.toString())

        response.close()*/
    }
}



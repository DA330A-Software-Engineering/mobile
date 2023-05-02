package com.HomeApp.ui.composables

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
fun EditDeviceState(
    modifier: Modifier = Modifier,
    deviceItem: DocumentSnapshot,
    type: String,
    onDelEdit: (Boolean) -> Unit
) {
    val state = deviceItem.get("state") as Map<*, *>
    val context: Context = LocalContext.current
    val coroutine = rememberCoroutineScope()

    var screenText by remember { mutableStateOf(state["text"] as String) }
    var tune by remember { mutableStateOf("") }
    //if (type == "screen") screenText = state["text"] as String
    //Log.d("LOOOOOOOOOK", state["text"] as String)


    Column(modifier = Modifier) {
        if (type == "screen") {
            Text(
                text = "Edit screen text",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            TextField(
                value = screenText,
                onValueChange = { if (it.length <= 16) screenText = it },
                singleLine = true,
                maxLines = 1
            )
        } else if (type == "buzzer") {
            Text(
                text = "Select a tune",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
        ) {
            Button(onClick = {
                onDelEdit(false)
                updateState(
                    context = context,
                    coroutine = coroutine,
                    newState = if (type == "screen") screenText else tune,
                    type = type,
                    deviceItem = deviceItem
                )
            }) {
                Text(text = if (type == "screen") "Update text" else "Play tune")
            }
            Button(onClick = { onDelEdit(false) }) {
                Text(text = "close")
            }
        }


    }
}

private fun updateState(
    context: Context,
    coroutine: CoroutineScope,
    newState: String,
    type: String,
    deviceItem: DocumentSnapshot
) {
    val updateState = JSONObject()


    if (type == "buzzer") updateState.put("tune", newState)
    else if (type == "screen") updateState.put("text", newState)

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
            id = deviceItem.id,
            state = updateState,
            type = type,
            onRespond = changeDeviceState
        )
    }


}


package com.HomeApp.ui.composables

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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

    var screenText by remember { mutableStateOf(if (type == "screen") state["text"] as String else "") }
    var tune by remember { mutableStateOf("") }
    val radioOptions = listOf("pirate", "alarm")
    var selectedOptionIndex by remember { mutableStateOf(-1) }
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
            Column {
                radioOptions.forEachIndexed { index, option ->
                    //tune = option
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedOptionIndex == index,
                            onClick = {
                                selectedOptionIndex = index
                                tune = option
                            }
                        )
                        Text(
                            text = option,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(50.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
        ) {
            Spacer(modifier = Modifier.weight(0.2f))
            Button(
                onClick = {
                    if (type == "buzzer" && selectedOptionIndex in radioOptions.indices) {
                        onDelEdit(false)
                        updateState(
                            context = context,
                            coroutine = coroutine,
                            newState = tune,
                            type = type,
                            deviceItem = deviceItem
                        )
                    } else if (type == "screen") {
                        onDelEdit(false)
                        updateState(
                            context = context,
                            coroutine = coroutine,
                            newState = screenText,
                            type = type,
                            deviceItem = deviceItem
                        )
                    }

                },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = if (type == "screen") "Update text" else "Play tune")
            }
            Spacer(modifier = Modifier.width(5.dp))
            Button(onClick = { onDelEdit(false) }, modifier = Modifier.weight(1f)) {
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
    Log.d("LOOOOOOOK", newState)

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


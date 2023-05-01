package com.HomeApp.ui.composables

import android.content.Context
import android.graphics.Paint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.HomeApp.util.ApiResult
import com.HomeApp.util.HttpStatus
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.CoroutineScope

@Composable
fun EditDeviceState(
    modifier: Modifier = Modifier,
    deviceItem: DocumentSnapshot,
    type: String,
    onDelEdit: (Boolean) -> Unit
){
    val state = deviceItem.get("state") as Map<*, *>
    val context: Context = LocalContext.current
    val coroutine = rememberCoroutineScope()

    var screenText by remember { mutableStateOf("") }
    if (type == "screen") screenText = state["text"] as String
    //Log.d("LOOOOOOOOOK", state["text"] as String)


    Column(modifier = Modifier) {
        if (type == "screen") {
            Text(text = "Edit screen text", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            TextField(value = screenText, onValueChange = { if (it.length <= 16)screenText = it}, singleLine = true,  maxLines = 1)
        }else if (type == "buzzer"){
            Text(text = "Select a tune", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)) {
            Button(onClick = { /*TODO*/ }) {
                Text(text = if (type == "screen") "Update text" else "Play tune")
            }
            Button(onClick = { /*TODO*/ }) {
                Text(text = "close")
            }
        }


    }
}

private fun updateState(
    context: Context,
    coroutine: CoroutineScope,
    deviceItem: DocumentSnapshot
) {

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

    

}


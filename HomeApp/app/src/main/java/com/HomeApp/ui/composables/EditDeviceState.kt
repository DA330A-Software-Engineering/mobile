package com.HomeApp.ui.composables

import android.graphics.Paint
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.DocumentSnapshot

@Composable
fun EditDeviceState(
    modifier: Modifier = Modifier,
    deviceItem: DocumentSnapshot,
    type: String
){
    val state = deviceItem.get("state") as Map<*, *>

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


    }
}


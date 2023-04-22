package com.HomeApp.ui.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.HomeApp.realTimeData
import com.HomeApp.ui.theme.RaminGrey
import com.HomeApp.util.*
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


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
    val deviceList = groupItem.get("devices") as ArrayList<*>
    val state: Map<String, Any> = emptyMap()
    var groupStateBool by remember {
        mutableStateOf(true)
    }
    var groupType by remember {
        mutableStateOf("")
    }

    LaunchedEffect(groupStateBool){
        for (device in deviceList) {
            getDocument("devices", device as String) { doc ->
                if (doc != null) {
                    groupType = doc.get("type") as String
                    val deviceState = doc.get("state") as Map<*, *>
                    if (groupType == "openLock" && !(deviceState["open"] as Boolean)) {
                        groupStateBool = false
                        groupType = "openLock"
                    } else if (groupType == "toggle" && !(deviceState["on"] as Boolean)) {
                        groupStateBool = false
                        groupType = "toggle"
                    }
                    else if (groupType == "fan" && !(deviceState["on"] as Boolean)) {
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
        onClick = { }, modifier = Modifier
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
            AlertDialog( // use Dismissbutton to have a composable with two buttons, for dismiss and delete
                onDismissRequest = { editGroup = false },
                title = { Text(groupItem.get("name") as String) },
                text = { EditGroup(groupItem = groupItem, onDelEdit = {newState -> editGroup = newState}) },
                confirmButton = {
                    Button(
                        onClick = { editGroup = false },
                    ) {
                        Text("Close")
                    }
                }
            )
        }

    }
}

private fun changeGroupState() {

}



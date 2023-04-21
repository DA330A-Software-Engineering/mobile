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
            .width(110.dp)
            .height(100.dp),
        contentPadding = PaddingValues(start = 0.dp, top = 8.dp, end = 0.dp, bottom = 0.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 15.dp)) {
            Text(
                text = groupItem.get("name") as String,
                modifier = Modifier.fillMaxWidth(),
                textDecoration = TextDecoration.Underline,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(15.dp))
            Row(modifier = Modifier) {
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
                text = { EditGroup(groupItem = groupItem) },
                confirmButton = {
                    Button(
                        onClick = { editGroup = false },
                    ) {
                        Text("Done")
                    }
                }
            )
        }

    }
}

@Composable
private fun EditGroup(
    modifier: Modifier = Modifier,
    groupItem: DocumentSnapshot,
) {
    var groupName by remember { mutableStateOf(groupItem.get("name") as String) }
    var groupDesc by remember { mutableStateOf(groupItem.get("description") as String) }

    Column(modifier = Modifier, verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Row(
            modifier = Modifier
                .height(50.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Name", modifier = Modifier.weight(2f))
            Spacer(modifier = Modifier.weight(0.1f))
            TextField(
                value = groupName, onValueChange = { groupName = it }, modifier = Modifier
                    .width(100.dp)
                    .weight(5f)
            )
        }
        Row(
            modifier = Modifier
                .height(50.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Description", modifier = Modifier.weight(2f))
            Spacer(modifier = Modifier.weight(0.1f))
            TextField(
                value = groupDesc, onValueChange = { groupDesc = it }, modifier = Modifier
                    .width(100.dp)
                    .weight(5f)
            )
        }
        val deviceList = groupItem.get("devices") as ArrayList<*>
        var firstDevice: DocumentSnapshot
        var groupType by remember {
            mutableStateOf("")
        }

        getDocument("devices", deviceList.first() as String) { doc ->
            if (doc != null) {
                firstDevice = doc
                groupType = doc.get("type") as String
            }
        }

        Row(
            modifier = Modifier
                .padding(top = 10.dp),
        ) {
            Text(text = "Devices", modifier = Modifier.weight(2f))
            Spacer(modifier = Modifier.weight(0.1f))
            Column(modifier = Modifier.weight(5f)) {
                for (item in deviceList) {
                    DeviceItem(item = item as String, groupItem = groupItem, isInGroup = true)
                }
            }

        }
        Row(modifier = Modifier.padding(top = 10.dp)) {
            Spacer(modifier = Modifier.weight(2.1f))
            LazyColumn(modifier = Modifier.weight(5f)) {
                items(items = realTimeData!!.devices, key = { item -> item.id }) { item ->
                    val isInGroup = deviceList.any { it == item.id }
                    val isSameType = item.get("type") == groupType
                    /**Log.d(
                        "THIS IS THE STUFF",
                        "Item type: ${item.get("type")} ---- groupType : $groupType"
                    )*/
                    if (!isInGroup && isSameType) {
                        DeviceItem(item = item.id, groupItem = groupItem , isInGroup = false)
                    }
                }
            }
        }

    }
}

@Composable
fun DeviceItem(
    modifier: Modifier = Modifier,
    item: String,
    isInGroup: Boolean,
    addGroup: Boolean = false,
    groupItem: DocumentSnapshot? = null,
    onItemAdded: (String) -> Unit = {}
) { // groupItem is for when a device is getting deleted and an API call needs to be made
    //var device = mutableStateListOf<DocumentSnapshot>()
    val context = LocalContext.current
    val coroutine = rememberCoroutineScope()
    var device: DocumentSnapshot?
    var deviceID by remember {
        mutableStateOf("")
    }
    var deviceName by remember {
        mutableStateOf("")
    }
    var groupDevices by remember { mutableStateOf(groupItem?.get("devices") as ArrayList<String>) }
    getDocument("devices", item) { doc ->
        device = doc
        if (doc != null) {
            deviceName = doc.get("name") as String
            deviceID = doc.id
            //groupDevices = doc.get("devices") as Array<String>
        }
    }
    val onChangeDevices: (ApiResult) -> Unit = {
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
    ) {
        Text(text = deviceName, modifier = Modifier.weight(2f))
        Spacer(modifier = Modifier.weight(0.1f))
        if (isInGroup) {
            IconButton(
                onClick = {
                    groupDevices.remove(deviceID)
                    coroutine.launch(Dispatchers.IO) {
                        if (groupItem != null) {
                            ApiConnector.updateGroup(
                                name = groupItem.get("name") as String,
                                token = LocalStorage.getToken(context),
                                id = groupItem.id,
                                description = groupItem.get("description") as String,
                                devices = groupDevices,
                                onRespond = onChangeDevices
                            )
                        }
                    } }, modifier = Modifier
                    .weight(0.5f)
                    .size(40.dp)
            ) {
                Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete Icon")

            }
        } else {
            IconButton(
                onClick = {
                        groupDevices.add(deviceID)
                        coroutine.launch(Dispatchers.IO) {
                            if (groupItem != null) {
                                ApiConnector.updateGroup(
                                    name = groupItem.get("name") as String,
                                    token = LocalStorage.getToken(context),
                                    id = groupItem.id,
                                    description = groupItem.get("description") as String,
                                    devices = groupDevices,
                                    onRespond = onChangeDevices
                                )
                            }
                        }

                          }, modifier = Modifier
                    .weight(0.5f)
                    .size(40.dp)
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")

            }
        }

    }


}
package com.HomeApp.ui.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.HomeApp.util.*
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
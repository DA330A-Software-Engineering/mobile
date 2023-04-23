package com.HomeApp.ui.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.HomeApp.realTimeData
import com.HomeApp.util.*
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun EditGroup(
    modifier: Modifier = Modifier,
    groupItem: DocumentSnapshot,
    onDelEdit: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val coroutine = rememberCoroutineScope()
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
        val deviceList = groupItem.get("devices") as ArrayList<String>
        var firstDevice: DocumentSnapshot
        var groupType by remember {
            mutableStateOf("")
        }


        if (deviceList.size != 0){
            getDocument("devices", deviceList.first() as String) { doc ->
                if (doc != null) {
                    firstDevice = doc
                    groupType = doc.get("type") as String
                }
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
                    var isSameType = item.get("type") == groupType
                    if (deviceList.size == 0) isSameType = true

                    if (!isInGroup && isSameType) {
                        DeviceItem(item = item.id, groupItem = groupItem , isInGroup = false)
                    }
                }
            }
        }
        val onDeleteGroup: (ApiResult) -> Unit = {
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
        Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = {
                onDelEdit(false)
                coroutine.launch(Dispatchers.IO) {
                    ApiConnector.deleteGroup(
                        id = groupItem.id,
                        token = LocalStorage.getToken(context),
                        onRespond = onDeleteGroup
                    )
                }
            }) {
                Text(text = "Delete")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(onClick = {
                onDelEdit(false)
                coroutine.launch(Dispatchers.IO) {
                    ApiConnector.updateGroup(
                        name = groupName,
                        token = LocalStorage.getToken(context),
                        id = groupItem.id,
                        description = groupDesc,
                        devices = deviceList,
                        onRespond = onDeleteGroup
                    )
                } }) {
                Text(text = "Update")
            }
        }


    }
}

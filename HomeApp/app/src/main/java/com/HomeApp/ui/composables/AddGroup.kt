package com.HomeApp.ui.composables

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.HomeApp.screens.realTimeData
import com.HomeApp.util.*
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AddGroup(
    modifier: Modifier = Modifier,
    onCreate: (Boolean) -> Unit
) {
    var groupName by remember { mutableStateOf("") }
    var groupDesc by remember { mutableStateOf("") }
    val context = LocalContext.current
    val coroutine = rememberCoroutineScope()

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
        var deviceList by remember {
            mutableStateOf(mutableListOf<String>())
        }
        var firstDevice: DocumentSnapshot
        var groupType by remember {
            mutableStateOf("all")
        }
        if (deviceList.size > 0) {
            getDocument("devices", deviceList.first()) { doc ->
                if (doc != null) {
                    firstDevice = doc
                    groupType = doc.get("type") as String
                }
            }
        }

        Row(modifier = Modifier.padding(top = 10.dp)) {
            Text(text = "Devices", modifier = Modifier.weight(2f))
            Spacer(modifier = Modifier.weight(0.1f))
            LazyColumn(modifier = Modifier.weight(5f)) {
                items(items = realTimeData!!.devices, key = { item -> item.id }) { item ->
                    if (groupType == "all") AddDevice(deviceItem = item, deviceList = deviceList, onItemSelect = {newType -> groupType = newType})
                    else if(groupType == item.get("type") as String) AddDevice(deviceItem = item, deviceList = deviceList, onItemSelect = {newType -> groupType = newType})

                }
            }
        }
        val onCreateGroup: (ApiResult) -> Unit = {
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
                onCreate(false)
                coroutine.launch(Dispatchers.IO) {
                    ApiConnector.createGroup(
                        token = LocalStorage.getToken(context),
                        onRespond = onCreateGroup,
                        name = groupName,
                        description = groupDesc,
                        devices = deviceList
                    )
                }
            }) {
                Text(text = "Create")
            }
        }


    }
}

@Composable
private fun AddDevice(
    modifier: Modifier = Modifier,
    deviceItem: DocumentSnapshot,
    deviceList: MutableList<String>,
    onItemSelect: (String) -> Unit
){
    var isAdded by remember {
        mutableStateOf(false)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
    ) {
        Text(text = deviceItem.get("name") as String, modifier = Modifier.weight(2f))
        Spacer(modifier = Modifier.weight(0.1f))
        if (isAdded) {
            IconButton(
                onClick = {
                    deviceList.remove(deviceItem.id)
                    if (deviceList.size == 0) onItemSelect("all")
                    isAdded = !isAdded
                    Log.d("THIS IS INSANITY", deviceList.toString())
                }, modifier = Modifier
                    .weight(0.5f)
                    .size(40.dp)
            ) {
                Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete Icon")

            }
        } else {
            IconButton(
                onClick = {
                    deviceList.add(deviceItem.id)
                    isAdded = !isAdded
                    onItemSelect(deviceItem.get("type") as String)
                    Log.d("THIS IS INSANITY", deviceList.toString())
                }, modifier = Modifier
                    .weight(0.5f)
                    .size(40.dp)
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")

            }
        }

    }
}
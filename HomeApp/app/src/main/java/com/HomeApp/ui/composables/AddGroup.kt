package com.HomeApp.ui.composables

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.HomeApp.realTimeData
import com.HomeApp.util.getDocument
import com.google.firebase.firestore.DocumentSnapshot

@Composable
fun AddGroup(modifier: Modifier = Modifier) {
    var groupName by remember { mutableStateOf("") }
    var groupDesc by remember { mutableStateOf("") }

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
            mutableStateOf("")
        }
        if (deviceList.size > 0) {
            getDocument("devices", deviceList.first()) { doc ->
                if (doc != null) {
                    firstDevice = doc
                    groupType = doc.get("type") as String
                }
            }
        }


        /**Row(
            modifier = Modifier
                .padding(top = 10.dp),
        ) {
            Text(text = "Devices", modifier = Modifier.weight(2f))
            Spacer(modifier = Modifier.weight(0.1f))
            Column(modifier = Modifier.weight(5f)) {
                for (item in deviceList) {
                    DeviceItem(item = item as String, isInGroup = true)
                }
            }

        }*/
        Row(modifier = Modifier.padding(top = 10.dp)) {
            Text(text = "Devices", modifier = Modifier.weight(2f))
            Spacer(modifier = Modifier.weight(0.1f))
            LazyColumn(modifier = Modifier.weight(5f)) {
                items(items = realTimeData!!.devices, key = { item -> item.id }) { item ->
                    DeviceItem(item = item.id, isInGroup = false)

                }
            }
        }

}
}
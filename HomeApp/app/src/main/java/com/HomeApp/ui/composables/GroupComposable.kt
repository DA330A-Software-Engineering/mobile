package com.HomeApp.ui.composables

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.Navigation
import com.HomeApp.ui.navigation.Settings
import com.HomeApp.ui.theme.RaminGrey
import com.HomeApp.util.getDocument
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
    Button(onClick = { /*TODO*/ }, modifier = Modifier
        .border(
            width = 1.dp,
            shape = RoundedCornerShape(10.dp),
            color = RaminGrey
        )
        .width(110.dp)
        .height(100.dp),
        contentPadding = PaddingValues(start = 0.dp, top = 8.dp, end = 0.dp, bottom = 0.dp),
        shape = RoundedCornerShape(10.dp)) {
        Column(modifier = Modifier.padding(horizontal = 15.dp)) {
            Text(text = groupItem.get("name") as String, modifier = Modifier.fillMaxWidth(), textDecoration = TextDecoration.Underline, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(15.dp))
            Row(modifier = Modifier) {
                Text(text = "On", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
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
            AlertDialog(
                onDismissRequest = { editGroup = false },
                title = { Text(groupItem.get("name") as String) },
                text = { editGroup(groupItem = groupItem)},
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
private fun editGroup(
    modifier: Modifier= Modifier,
    groupItem: DocumentSnapshot,
) {
    var groupName by remember { mutableStateOf(groupItem.get("name") as String) }
    var groupDesc by remember { mutableStateOf(groupItem.get("description") as String) }

    Column(modifier = Modifier, verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Row(modifier = Modifier
            .height(50.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Name", modifier = Modifier.weight(2f))
            Spacer(modifier = Modifier.weight(0.1f))
            TextField(value = groupName, onValueChange = {groupName = it}, modifier = Modifier
                .width(100.dp)
                .weight(5f))
        }
        Row(modifier = Modifier
            .height(50.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Description", modifier = Modifier.weight(2f))
            Spacer(modifier = Modifier.weight(0.1f))
            TextField(value = groupDesc, onValueChange = {groupDesc = it}, modifier = Modifier
                .width(100.dp)
                .weight(5f))
        }
        val coroutine = rememberCoroutineScope()
        Row(modifier = Modifier
            .height(50.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Devices", modifier = Modifier.weight(2f))
            Spacer(modifier = Modifier.weight(0.1f))
            Column(modifier = Modifier) {
                for (item in groupItem.get("devices") as ArrayList<*>){
                    //var device = mutableStateListOf<DocumentSnapshot>()
                    var device: DocumentSnapshot?
                    var deviceName by remember {
                        mutableStateOf("")
                    }
                    getDocument("devices", item as String) { doc ->
                        device = doc
                        if (doc != null) {
                            deviceName = doc.get("name") as String
                        }
                    }
                    Row(modifier = Modifier) {
                        Text(text = deviceName)
                    }


                }
            }
        }




    }
}
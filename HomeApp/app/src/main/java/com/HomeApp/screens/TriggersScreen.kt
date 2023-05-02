package com.HomeApp.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.HomeApp.ui.composables.DeleteDialog
import com.HomeApp.ui.composables.TopTitleBar
import com.HomeApp.ui.composables.TopTitleBarItem
import com.HomeApp.ui.theme.DarkRed
import com.HomeApp.util.ApiResult
import com.HomeApp.util.LocalStorage
import com.google.firebase.firestore.DocumentSnapshot

@Composable
fun TriggersScreen(
    navController: NavController,
    OnSelfClick: () -> Unit = {}
) {
    SelectedItems.setIsSensor(true)

    val listHeight = LocalConfiguration.current.screenHeightDp
    val documents = realTimeData!!.triggers

    Scaffold(
        topBar = {
            TopTitleBar(
                item = TopTitleBarItem.Triggers,
                navController = navController
            )
        },
        content = {
            LazyColumn(
                modifier = Modifier
                    .height(listHeight.dp)
                    .padding(it)
                    .padding(vertical = 10.dp)
                    .padding(top = 10.dp)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(items = documents, key = { item -> item.id }) { item ->
                    TriggerCard(triggerItem = item)
                }
            }
        }
    )
}

@Composable
private fun TriggerCard(triggerItem: DocumentSnapshot) {
    val context = LocalContext.current
    val token = LocalStorage.getToken(context)

    val id = remember { mutableStateOf(triggerItem.id) }
    val name = remember { mutableStateOf(triggerItem.get("name") as String) }

    val onRespond: (ApiResult) -> Unit = {
        Log.d("RESPOND", it.toString())
    }

    val deleteDialog = remember { mutableStateOf(false) }
    if (deleteDialog.value) {
        DeleteDialog(
            deleteDialog = deleteDialog,
            name = name.value,
            token = token,
            id = id.value,
            onRespond = onRespond
        )
    }

    Row {
        Text(text = name.value)
        IconButton(
            modifier = Modifier.scale(1.4f),
            onClick = { deleteDialog.value = true }
        ) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "delete-icon",
                tint = DarkRed
            )
        }
    }
}
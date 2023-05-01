package com.HomeApp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.HomeApp.ui.composables.TopTitleBar
import com.HomeApp.ui.composables.TopTitleBarItem
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
    Text(text = triggerItem.id)
}
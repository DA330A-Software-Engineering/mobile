package com.HomeApp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.HomeApp.ui.composables.DeviceCard
import com.HomeApp.ui.composables.RoutinesTitleBar
import com.HomeApp.ui.composables.RoutinesTitleBarItem

@Composable
fun ChooseGroupsScreen(
    navController: NavController,
    OnSelfClick: () -> Unit = {}
) {
    val listHeight = LocalConfiguration.current.screenHeightDp
    val documents = rememberFirestoreCollection("groups", Groupss::class.java, "groups")

    Scaffold(
        topBar = {
                 RoutinesTitleBar(item = RoutinesTitleBarItem.ChooseGroups, navController = navController)
        },
        content = {
            LazyColumn(
                modifier = androidx.compose.ui.Modifier
                    .height(listHeight.dp)
                    .padding(it)
                    .padding(vertical = 10.dp)
                    .padding(top = 10.dp)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                content = {
                    items(items = documents, key = { item -> item.id }) { item ->
                        DeviceCard(navController = navController, deviceItem = item)
                    }
                }
            )
        }
    )
}
package com.HomeApp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.HomeApp.ui.composables.RoutinesTitleBar
import com.HomeApp.ui.composables.RoutinesTitleBarItem
import com.HomeApp.ui.navigation.ChooseSchedule
import com.HomeApp.ui.theme.GhostWhite

@Composable
fun ChooseScheduleScreen(
    navController: NavController,
    OnSelfClick: () -> Unit = {}
) {
    val listHeight = LocalConfiguration.current.screenHeightDp
    val documents = SelectedItems.getItems(devices = true)

    Scaffold(
        topBar = {
            RoutinesTitleBar(
                item = RoutinesTitleBarItem.ChooseSchedule,
                navController = navController
            )
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

                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.scale(1f),
                onClick = { navController.navigate(ChooseSchedule.route) },
                backgroundColor = GhostWhite,
                content = {
                    Icon(
                        modifier = Modifier.scale(1.4f),
                        imageVector = Icons.Rounded.ArrowForward,
                        contentDescription = "arrow-icon",
                        tint = Color.Black
                    )
                },
            )
        },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.End
    )
}

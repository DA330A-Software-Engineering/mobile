package com.HomeApp.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.HomeApp.screens.SelectedItems
import com.HomeApp.ui.navigation.Devices
import com.HomeApp.ui.navigation.Home
import com.HomeApp.ui.navigation.Triggers

@Composable
fun TopTitleBar(
    item: TopTitleBarItem,
    navController: NavController
) {
    val isDevices = SelectedItems.getIsDevices()
    val showDialog = remember { mutableStateOf(false) }
    if (showDialog.value) {
        Dialog(
            showDialog = showDialog,
            navController = navController,
            route = item.routeRightButton!!
        )
    }

    Column(modifier = Modifier) {
        Spacer(modifier = Modifier.height(30.dp))
        Row(
            modifier = Modifier.height(60.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate(item.routeLeftButton) }
            ) {
                Icon(
                    modifier = Modifier.scale(2.2f),
                    imageVector = item.iconLeft,
                    contentDescription = "left-icon"
                )
            }
            Text(
                text = if (item == TopTitleBarItem.ChooseItems) {
                    if (isDevices) "Devices" else "Groups"
                } else {
                    item.title
                },
                modifier = Modifier.weight(2f),
                fontSize = 35.sp,
                textAlign = TextAlign.Center
            )
            IconButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    if (item.iconRight == Icons.Rounded.Close) {
                        showDialog.value = true
                    } else {
                        navController.navigate(item.routeRightButton!!)
                    }
                }
            ) {
                if (item.iconRight != null) {
                    Icon(
                        modifier = Modifier.scale(2.2f),
                        imageVector = item.iconRight,
                        contentDescription = "right-icon"
                    )
                } else {
                    Box(modifier = Modifier.scale(2.2f))
                }
            }
        }
    }
}

@Composable
private fun Dialog(
    showDialog: MutableState<Boolean>,
    navController: NavController,
    route: String
) {
    AlertDialog(
        onDismissRequest = { showDialog.value = false },
        title = {
            Column {
                Text(
                    text = "Are you sure you want to exit?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(text = "All progress will be lost", fontSize = 15.sp)
            }
        },
        confirmButton = {
            TextButton(onClick = { navController.navigate(route) }) {
                Text(text = "Exit")
            }
        },
        dismissButton = {
            TextButton(onClick = { showDialog.value = false }) {
                Text(text = "Cancel")
            }
        }
    )
}

sealed class TopTitleBarItem(
    val title: String,
    val iconLeft: ImageVector,
    val routeLeftButton: String,
    val iconRight: ImageVector?,
    val routeRightButton: String?
) {
    object Routines : TopTitleBarItem(
        title = "Routines",
        iconLeft = Icons.Rounded.ArrowBack,
        routeLeftButton = Home.route,
        iconRight = Icons.Rounded.Add,
        routeRightButton = com.HomeApp.ui.navigation.ChooseType.route
    )

    object ChooseType : TopTitleBarItem(
        title = "Choose",
        iconLeft = Icons.Rounded.ArrowBack,
        routeLeftButton = if (SelectedItems.getIsSensor()) {
            com.HomeApp.ui.navigation.Triggers.route
        } else {
            com.HomeApp.ui.navigation.Routines.route
        },
        iconRight = Icons.Rounded.Close,
        routeRightButton = if (SelectedItems.getIsSensor()) {
            com.HomeApp.ui.navigation.Triggers.route
        } else {
            com.HomeApp.ui.navigation.Routines.route
        }
    )

    object ChooseItems : TopTitleBarItem(
        title = "Items",
        iconLeft = Icons.Rounded.ArrowBack,
        routeLeftButton = com.HomeApp.ui.navigation.ChooseType.route,
        iconRight = Icons.Rounded.Close,
        routeRightButton = if (SelectedItems.getIsSensor()) {
            com.HomeApp.ui.navigation.Triggers.route
        } else {
            com.HomeApp.ui.navigation.Routines.route
        }
    )

    object ChooseActions : TopTitleBarItem(
        title = "Actions",
        iconLeft = Icons.Rounded.ArrowBack,
        routeLeftButton = com.HomeApp.ui.navigation.ChooseItems.route,
        iconRight = Icons.Rounded.Close,
        routeRightButton = if (SelectedItems.getIsSensor()) {
            com.HomeApp.ui.navigation.Triggers.route
        } else {
            com.HomeApp.ui.navigation.Routines.route
        }
    )

    object ChooseSchedule : TopTitleBarItem(
        title = "Schedule",
        iconLeft = Icons.Rounded.ArrowBack,
        routeLeftButton = com.HomeApp.ui.navigation.ChooseActions.route,
        iconRight = Icons.Rounded.Close,
        routeRightButton = com.HomeApp.ui.navigation.Routines.route
    )

    object Finish : TopTitleBarItem(
        title = "Finish",
        iconLeft = Icons.Rounded.ArrowBack,
        routeLeftButton = if (SelectedItems.getIsSensor()) {
            com.HomeApp.ui.navigation.ChooseActions.route
        } else {
            com.HomeApp.ui.navigation.ChooseSchedule.route
        },
        iconRight = Icons.Rounded.Close,
        routeRightButton = if (SelectedItems.getIsSensor()) {
            com.HomeApp.ui.navigation.Triggers.route
        } else {
            com.HomeApp.ui.navigation.Routines.route
        }
    )

    object Triggers : TopTitleBarItem(
        title = "Triggers",
        iconLeft = Icons.Rounded.ArrowBack,
        routeLeftButton = Devices.route,
        iconRight = Icons.Rounded.Add,
        routeRightButton = com.HomeApp.ui.navigation.ChooseType.route
    )

    object EditTrigger : TopTitleBarItem(
        title = "Edit",
        iconLeft = Icons.Rounded.ArrowBack,
        routeLeftButton = com.HomeApp.ui.navigation.Triggers.route,
        iconRight = null,
        routeRightButton = null
    )
}
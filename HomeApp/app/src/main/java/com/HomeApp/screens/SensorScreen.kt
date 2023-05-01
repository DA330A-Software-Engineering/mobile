package com.HomeApp.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.DoorFront
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material.icons.outlined.SmartScreen
import androidx.compose.material.icons.outlined.SurroundSound
import androidx.compose.material.icons.outlined.Window
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.HomeApp.ui.composables.RoutinesTitleBar
import com.HomeApp.ui.composables.RoutinesTitleBarItem
import com.HomeApp.ui.navigation.ChooseActions
import com.HomeApp.ui.navigation.SensorAction
import com.HomeApp.ui.theme.LightSteelBlue
import com.google.firebase.firestore.DocumentSnapshot

@Composable
fun SensorScreen(
    navController: NavController,
    OnSelfClick: () -> Unit = {}
) {
    val listHeight = LocalConfiguration.current.screenHeightDp
    val devices: MutableList<DocumentSnapshot> = mutableListOf()
    realTimeData!!.devices.forEach {
        if (it.get("type") != "sensor") devices.add(it)
    }

    Scaffold(
        topBar = {
            RoutinesTitleBar(
                item = RoutinesTitleBarItem.Sensor,
                navController = navController
            )
        },
        content = {
            LazyColumn(
                modifier = Modifier
                    .height(listHeight.dp)
                    .padding(it)
                    .padding(vertical = 10.dp, horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                content = {
                    item { Text(
                        text = "Choose a device to affect when the sensor is triggered",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    ) }
                    items(items = devices, key = { item -> item.id }) { item ->
                        ChooseDeviceCard(device = item, navController = navController)
                    }
                }
            )
        }
    )
}

@Composable
private fun ChooseDeviceCard(
    device: DocumentSnapshot,
    navController: NavController
) {
    val name = device.get("name") as String
    val cardIcon = when (device.get("type")) {
        "toggle" -> Icons.Filled.Lightbulb
        "screen" -> Icons.Outlined.SmartScreen
        "buzzer" -> Icons.Outlined.SurroundSound
        "fan" -> Icons.Outlined.RestartAlt
        "openLock" -> if (device.get("tag") == "door") Icons.Filled.DoorFront else Icons.Outlined.Window
        else -> Icons.Filled.BrokenImage
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable(onClick = {
                SelectedItems.setType(isDevices = true, isSensor = true)
                SelectedItems.clear()
                SelectedItems.modifySelection(device)
                navController.navigate(SensorAction.route)
            }),
        backgroundColor = LightSteelBlue
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier
                    .weight(1f)
                    .size(48.dp),
                imageVector = cardIcon,
                contentDescription = "$name-icon",
            )
            Text(
                modifier = Modifier
                    .weight(5f)
                    .padding(start = 5.dp),
                text = name,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
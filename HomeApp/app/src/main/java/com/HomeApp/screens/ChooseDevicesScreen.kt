package com.HomeApp.screens

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
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.DoorFront
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.outlined.SmartScreen
import androidx.compose.material.icons.outlined.SurroundSound
import androidx.compose.material.icons.outlined.Window
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.HomeApp.ui.composables.RoutinesTitleBar
import com.HomeApp.ui.composables.RoutinesTitleBarItem
import com.HomeApp.ui.navigation.ChooseActions
import com.HomeApp.ui.theme.GhostWhite
import com.HomeApp.ui.theme.LightSteelBlue
import com.google.firebase.firestore.DocumentSnapshot

data class SelectedItemsData(
    var selectedDevices: List<DocumentSnapshot> = emptyList(),
    var selectedGroups: List<DocumentSnapshot> = emptyList()
)

object SelectedItems {
    private var selectedItemsData: SelectedItemsData = SelectedItemsData()

    fun addItem(item: DocumentSnapshot, devices: Boolean) {
        if (devices) selectedItemsData.selectedDevices += item
        else selectedItemsData.selectedGroups += item
    }

    fun removeItem(item: DocumentSnapshot, devices: Boolean) {
        if (devices) selectedItemsData.selectedDevices -= item
        else selectedItemsData.selectedGroups -= item

    }

    fun getItems(devices: Boolean): List<DocumentSnapshot> {
        if (devices) return selectedItemsData.selectedDevices
        return selectedItemsData.selectedGroups
    }

    fun clearItems() {
        selectedItemsData.selectedDevices = emptyList()
        selectedItemsData.selectedGroups = emptyList()
    }
}

@Composable
fun ChooseDevicesScreen(
    navController: NavController,
    OnSelfClick: () -> Unit = {}
) {
    val listHeight = LocalConfiguration.current.screenHeightDp
    val documents = rememberFirestoreCollection("devices", Devices::class.java, "devices")

    Scaffold(
        topBar = {
            RoutinesTitleBar(
                item = RoutinesTitleBarItem.ChooseDevices,
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
                verticalArrangement = Arrangement.spacedBy(10.dp),
                content = {
                    items(items = documents, key = { item -> item.id }) { item ->
                        SelectDeviceCard(deviceItem = item)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.scale(1f),
                onClick = { navController.navigate(ChooseActions.route) },
                backgroundColor = GhostWhite
            ) {
                Icon(
                    modifier = Modifier.scale(1.4f),
                    imageVector = Icons.Rounded.ArrowForward,
                    contentDescription = "arrow-icon",
                    tint = Color.Black
                )
            }
        },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.End
    )
}

@Composable
private fun SelectDeviceCard(
    deviceItem: DocumentSnapshot
) {
    val cardIcon: ImageVector = when (deviceItem.get("type")) {
        "toggle" -> Icons.Filled.Lightbulb
        "door" -> Icons.Filled.DoorFront
        "window" -> Icons.Outlined.Window
        "screen" -> Icons.Outlined.SmartScreen
        "buzzer" -> Icons.Outlined.SurroundSound
        else -> Icons.Filled.BrokenImage
    }

    val name = deviceItem.get("name") as String
    val check = remember { mutableStateOf(SelectedItems.getItems(devices = true).contains(deviceItem)) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable(onClick = {
                check.value = !check.value
                if (SelectedItems
                        .getItems(devices = true)
                        .contains(deviceItem)
                ) {
                    SelectedItems.removeItem(item = deviceItem, devices = true)
                } else {
                    SelectedItems.addItem(item = deviceItem, devices = true)
                }
            }),
        backgroundColor = LightSteelBlue
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                modifier = Modifier.weight(2f),
                checked = check.value,
                onCheckedChange = {
                    check.value = !check.value
                    if (SelectedItems
                            .getItems(devices = true)
                            .contains(deviceItem)
                    ) {
                        SelectedItems.removeItem(item = deviceItem, devices = true)
                    } else {
                        SelectedItems.addItem(item = deviceItem, devices = true)
                    }
                })
            Icon(
                modifier = Modifier
                    .weight(1f)
                    .size(48.dp),
                imageVector = cardIcon,
                contentDescription = "$name-icon",
            )
            Text(
                modifier = Modifier
                    .weight(7f)
                    .padding(start = 5.dp),
                text = name,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
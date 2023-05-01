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
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.outlined.DoorFront
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material.icons.outlined.SmartScreen
import androidx.compose.material.icons.outlined.SurroundSound
import androidx.compose.material.icons.outlined.Window
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.HomeApp.ui.composables.RoutinesFAB
import com.HomeApp.ui.composables.TopTitleBar
import com.HomeApp.ui.composables.TopTitleBarItem
import com.HomeApp.ui.navigation.ChooseActions
import com.HomeApp.ui.theme.LightSteelBlue
import com.HomeApp.util.getDocument
import com.google.firebase.firestore.DocumentSnapshot

data class SelectedItemsData(
    var selectedItems: List<DocumentSnapshot> = emptyList(),
    var isDevices: Boolean = true,
    var isSensor: Boolean = true,
    var sensorId: String = ""
)

object SelectedItems {
    private var selectedItemsData: SelectedItemsData = SelectedItemsData()

    fun setItems(item: DocumentSnapshot) {
        if (selectedItemsData.selectedItems.contains(item)) {
            selectedItemsData.selectedItems -= item
        } else {
            selectedItemsData.selectedItems += item
        }
    }

    fun getItems(): List<DocumentSnapshot> {
        return selectedItemsData.selectedItems
    }

    fun setIsDevices(isDevices: Boolean) {
        selectedItemsData.isDevices = isDevices
    }

    fun getIsDevices(): Boolean {
        return selectedItemsData.isDevices
    }

    fun setIsSensor(isSensor: Boolean) {
        selectedItemsData.isSensor = isSensor
    }

    fun getIsSensor(): Boolean {
        return selectedItemsData.isSensor
    }

    fun setSensorId(id: String) {
        selectedItemsData.sensorId = id
    }

    fun getSensorId(): String {
        return selectedItemsData.sensorId
    }

    fun clear() {
        selectedItemsData.selectedItems = emptyList()
    }
}

@Composable
fun ChooseItemsScreen(
    navController: NavController,
    OnSelfClick: () -> Unit = {}
) {
    Actions.clear()
    val listHeight = LocalConfiguration.current.screenHeightDp
    val isDevices = SelectedItems.getIsDevices()
    val isSensor = SelectedItems.getIsSensor()
    val documents = mutableListOf<DocumentSnapshot>()
    if (isDevices == false) {
        realTimeData!!.groups.forEach {
            documents.add(it)
        }
    } else {
        realTimeData!!.devices.forEach {
            if (it.get("type") != "sensor") {
                documents.add(it)
            }
        }
    }

    Scaffold(
        topBar = {
            TopTitleBar(
                item = TopTitleBarItem.ChooseItems,
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
                    if (isSensor == true) {
                        item {
                            Text(
                                text = "Choose devices to affect when the sensor is triggered",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    items(items = documents, key = { item -> item.id }) { item ->
                        SelectItemCard(document = item)
                    }
                }
            )
        },
        floatingActionButton = {
            RoutinesFAB(
                icon = Icons.Rounded.ArrowForward,
                onClick = { navController.navigate(ChooseActions.route) }
            )
        },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.End
    )
}

@Composable
private fun SelectItemCard(document: DocumentSnapshot) {
    var type by remember { mutableStateOf("") }
    var tag by remember { mutableStateOf("") }

    val isDevices = SelectedItems.getIsDevices()
    if (isDevices) {
        type = document.get("type") as String
        if (type == "openLock") tag = document.get("tag") as String
    } else {
        val deviceIds = document.get("devices") as List<String>
        LaunchedEffect(deviceIds) {
            getDocument("devices", deviceIds[0]) { document ->
                if (document != null) {
                    type = document.get("type") as String
                    if (type == "openLock") tag = document.get("tag") as String
                }
            }
        }
    }

    val cardIcon = remember { mutableStateOf(Icons.Filled.BrokenImage) }
    LaunchedEffect(type, tag) {
        cardIcon.value = when (type) {
            "toggle" -> Icons.Outlined.Lightbulb
            "openLock" -> if (tag == "door") Icons.Outlined.DoorFront else Icons.Outlined.Window
            "screen" -> Icons.Outlined.SmartScreen
            "buzzer" -> Icons.Outlined.SurroundSound
            "fan" -> Icons.Outlined.RestartAlt
            else -> Icons.Filled.BrokenImage
        }
    }

    val name = document.get("name") as String
    val check = remember { mutableStateOf(SelectedItems.getItems().contains(document)) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable(onClick = {
                check.value = !check.value
                SelectedItems.setItems(document)
            }),
        backgroundColor = LightSteelBlue
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                modifier = Modifier.weight(2f),
                checked = check.value,
                onCheckedChange = {
                    check.value = !check.value
                    SelectedItems.setItems(document)
                }
            )
            Icon(
                modifier = Modifier
                    .weight(1f)
                    .size(48.dp),
                imageVector = cardIcon.value,
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
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.HomeApp.ui.composables.Actions
import com.HomeApp.ui.composables.RoutinesFAB
import com.HomeApp.ui.composables.RoutinesTitleBar
import com.HomeApp.ui.composables.RoutinesTitleBarItem
import com.HomeApp.ui.navigation.ChooseActions
import com.HomeApp.ui.theme.LightSteelBlue
import com.google.firebase.firestore.DocumentSnapshot

data class SelectedItemsData(
    var selectedItems: List<DocumentSnapshot> = emptyList(),
    var isDevice: Boolean = true,
    var isSensor: Boolean = false
)

object SelectedItems {
    private var selectedItemsData: SelectedItemsData = SelectedItemsData()

    fun modifySelection(item: DocumentSnapshot) {
        if (selectedItemsData.selectedItems.contains(item)) {
            selectedItemsData.selectedItems -= item
        } else {
            selectedItemsData.selectedItems += item
        }
    }

    fun getItems(): List<DocumentSnapshot> {
        return selectedItemsData.selectedItems
    }

    fun setType(isDevices: Boolean, isSensor: Boolean) {
        selectedItemsData.isDevice = isDevices
        selectedItemsData.isSensor = isSensor
    }

    fun getType(): List<Boolean> {
        return listOf(selectedItemsData.isDevice, selectedItemsData.isSensor)
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

    val isDevices = SelectedItems.getType()[0]
    val documents = if (isDevices) realTimeData!!.devices else realTimeData!!.groups

    Scaffold(
        topBar = {
            RoutinesTitleBar(
                item = RoutinesTitleBarItem.ChooseItems,
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
                    items(items = documents, key = { item -> item.id }) { item ->
                        SelectItemCard(document = item, isDevices = isDevices)
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
private fun SelectItemCard(
    document: DocumentSnapshot,
    isDevices: Boolean
) {

    var cardIcon: ImageVector = Icons.Filled.BrokenImage
    if (isDevices) {
        cardIcon = when (document.get("type")) {
            "toggle" -> Icons.Filled.Lightbulb
            "door" -> Icons.Filled.DoorFront
            "window" -> Icons.Outlined.Window
            "screen" -> Icons.Outlined.SmartScreen
            "buzzer" -> Icons.Outlined.SurroundSound
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
                SelectedItems.modifySelection(document)
            }),
        backgroundColor = LightSteelBlue
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                modifier = Modifier.weight(2f),
                checked = check.value,
                onCheckedChange = {
                    check.value = !check.value
                    SelectedItems.modifySelection(document)
                }
            )
            if (isDevices) {
                Icon(
                    modifier = Modifier
                        .weight(1f)
                        .size(48.dp),
                    imageVector = cardIcon,
                    contentDescription = "$name-icon",
                )
            }
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
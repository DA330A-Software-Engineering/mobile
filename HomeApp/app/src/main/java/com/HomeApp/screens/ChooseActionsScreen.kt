package com.HomeApp.screens

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.FabPosition
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.DoorFront
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.ModeFanOff
import androidx.compose.material.icons.filled.Window
import androidx.compose.material.icons.outlined.CompareArrows
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.SmartScreen
import androidx.compose.material.icons.outlined.SurroundSound
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Power
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
import com.HomeApp.ui.composables.RoutinesFAB
import com.HomeApp.ui.composables.RoutinesTitleBar
import com.HomeApp.ui.composables.RoutinesTitleBarItem
import com.HomeApp.ui.navigation.ChooseSchedule
import com.HomeApp.ui.theme.FadedLightGrey
import com.HomeApp.ui.theme.LightSteelBlue
import com.google.firebase.firestore.DocumentSnapshot
import kotlin.reflect.typeOf

data class Action(
    val id: String,
    val type: String,
    val state: Map<String, Boolean>
)

data class ActionsData(
    var actions: Array<Action> = emptyArray()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ActionsData

        if (!actions.contentEquals(other.actions)) return false

        return true
    }

    override fun hashCode(): Int {
        return actions.contentHashCode()
    }
}

object Actions {
    private var actionsData: ActionsData = ActionsData()

    fun addAction(id: String, type: String, state: Map<String, Boolean>) {
        val action = Action(id, type, state)
        val index = actionsData.actions.indexOfFirst { it.id == id }
        if (index != -1) {
            actionsData.actions = actionsData.actions.copyOf().apply {
                set(index, action)
            }
        } else {
            actionsData.actions += action
        }
    }

    fun getActions(): Array<Action> {
        return actionsData.actions
    }

    fun clearList() {
        actionsData.actions = emptyArray()
    }
}

@Composable
fun ChooseActionsScreen(
    navController: NavController,
    OnSelfClick: () -> Unit = {}
) {
    Schedule.clearCronString()
    val listHeight = LocalConfiguration.current.screenHeightDp
    val documents = SelectedItems.getItems()

    // Add all actions to a list, or else they would only be added when first shown on the screen.
    // I.e. when the user scrolls though the lazy column
    documents.forEach {
        val state = it.get("state") as MutableMap<String, Boolean>
        val keys = remember { mutableListOf<String>() }
        keys.clear()
        for (key in state.keys) {
            keys.add(key)
            keys.add(keys.removeAt(0)) // This makes the keys for door [open, locked] instead of [locked, open]
        }

        state[keys[0]] = true
        if (keys.size == 2) {
            state[keys[1]] = false // I want reverse and locked to be false initially
        }
        Actions.addAction(it.id, it.get("type") as String, state)
    }

    Scaffold(
        topBar = {
            RoutinesTitleBar(
                item = RoutinesTitleBarItem.ChooseActions,
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
                        ActionCards(deviceItem = item)
                    }
                }
            )
        },
        floatingActionButton = {
            RoutinesFAB(
                icon = Icons.Rounded.ArrowForward,
                onClick = { navController.navigate(ChooseSchedule.route) }
            )
        },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.End
    )
}

@Composable
private fun ActionCards(
    deviceItem: DocumentSnapshot
) {
    val type = deviceItem.get("type") as String

    val primaryOn: String = when (type) {
        "openLock" -> "Open"
        else -> "Turn On"
    }

    val primaryOff: String = when (type) {
        "toggle" -> "Turn off"
        "openLock" -> "Close"
        else -> "Turn off"
    }

    val secondaryOn: String? = when (type) {
        "openLock" -> "Unlock"
        "fan" -> "Normal"
        else -> null
    }

    val secondaryOff: String? = when (type) {
        "openLock" -> "Lock"
        "fan" -> "Reverse"
        else -> null
    }

    val cardIcon: ImageVector = when (type) {
        "toggle" -> Icons.Filled.Lightbulb
        "fan" -> Icons.Filled.ModeFanOff
        "screen" -> Icons.Outlined.SmartScreen
        "buzzer" -> Icons.Outlined.SurroundSound
        else -> Icons.Filled.BrokenImage
    }

    val actionIcon: ImageVector? = when (type) {
        "door", "window" -> Icons.Outlined.Lock
        "fan" -> Icons.Outlined.CompareArrows
        else -> null
    }

    val primaryCheck = remember { mutableStateOf(true) }
    val selectedPrimary = if (primaryCheck.value) LightSteelBlue else FadedLightGrey
    val notSelectedPrimary = if (!primaryCheck.value) LightSteelBlue else FadedLightGrey

    val secondaryCheck = remember { mutableStateOf(true) }
    val selectedSecondary = if (secondaryCheck.value) LightSteelBlue else FadedLightGrey
    val notSelectedSecondary = if (!secondaryCheck.value) LightSteelBlue else FadedLightGrey

    val id = deviceItem.id
    val state = deviceItem.get("state") as MutableMap<String, Boolean>
    val keys = remember { mutableListOf<String>() }
    keys.clear()
    for (key in state.keys) {
        keys.add(key)
        keys.add(keys.removeAt(0)) // This makes the keys for door [open, locked] instead of [locked, open]
    }

    state[keys[0]] = primaryCheck.value
    if (keys.size == 2) {
        state[keys[1]] = !secondaryCheck.value // I want reverse and locked to be false initially
    }
    Actions.addAction(id, type, state)

    val name = deviceItem.get("name") as String

    Column(
        Modifier
            .border(
                width = 4.dp,
                color = LightSteelBlue,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier
                    .weight(1f)
                    .size(48.dp),
                imageVector =
                if (deviceItem.get("tag") == "door") {
                    Icons.Filled.DoorFront
                } else if (deviceItem.get("tag") == "window") {
                    Icons.Filled.Window
                } else {
                    cardIcon
                },
                contentDescription = "$name-icon"
            )
            Text(
                modifier = Modifier.weight(8f),
                text = name,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            text = "Choose an action to execute",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
        Column(
            Modifier
                .fillMaxSize()
                .padding(top = 5.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                        .clickable(onClick = { primaryCheck.value = false }),
                    backgroundColor = notSelectedPrimary,
                ) {
                    Column(
                        Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = primaryOff,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            modifier = Modifier.weight(2f),
                            imageVector = Icons.Rounded.Power,
                            contentDescription = "on-icon"
                        )
                    }
                }
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                        .clickable(onClick = { primaryCheck.value = true }),
                    backgroundColor = selectedPrimary,
                ) {
                    Column(
                        Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = primaryOn,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            modifier = Modifier.weight(2f),
                            imageVector = Icons.Rounded.Power,
                            contentDescription = "off-icon"
                        )
                    }
                }
            }
            if (secondaryOn != null && secondaryOff != null) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(100.dp)
                            .clickable(onClick = { secondaryCheck.value = false }),
                        backgroundColor = notSelectedSecondary,
                    ) {
                        Column(
                            Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                modifier = Modifier.weight(1f),
                                text = secondaryOff,
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                modifier = Modifier.weight(2f),
                                imageVector = Icons.Rounded.Power,
                                contentDescription = "off-icon"
                            )
                        }
                    }
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(100.dp)
                            .clickable(onClick = { secondaryCheck.value = true }),
                        backgroundColor = selectedSecondary,
                    ) {
                        Column(
                            Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                modifier = Modifier.weight(1f),
                                text = secondaryOn,
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                modifier = Modifier.weight(2f),
                                imageVector = Icons.Rounded.Power,
                                contentDescription = "off-icon"
                            )
                        }
                    }
                }
            }
        }
    }
}

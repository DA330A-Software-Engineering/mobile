package com.HomeApp.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material.icons.filled.Power
import androidx.compose.material.icons.filled.PowerOff
import androidx.compose.material.icons.outlined.DoorFront
import androidx.compose.material.icons.outlined.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.outlined.KeyboardDoubleArrowRight
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material.icons.outlined.Power
import androidx.compose.material.icons.outlined.PowerOff
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material.icons.outlined.Sensors
import androidx.compose.material.icons.outlined.SmartScreen
import androidx.compose.material.icons.outlined.SurroundSound
import androidx.compose.material.icons.outlined.Window
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.HomeApp.ui.composables.CustomFAB
import com.HomeApp.ui.composables.TopTitleBar
import com.HomeApp.ui.composables.TopTitleBarItem
import com.HomeApp.ui.navigation.ChooseSchedule
import com.HomeApp.ui.navigation.Finish
import com.HomeApp.ui.theme.FadedLightGrey
import com.HomeApp.ui.theme.LightSteelBlue
import com.HomeApp.util.getDocument
import com.google.firebase.firestore.DocumentSnapshot
import org.json.JSONArray
import org.json.JSONObject

data class ActionsData(
    var actions: JSONArray = JSONArray()
)

object Actions {
    private var actionsData: ActionsData = ActionsData()

    fun addAction(id: String, type: String, state: Map<String, Boolean>) {
        val isDevices = SelectedItems.getIsDevices()
        val action = JSONObject()
            .put(if (isDevices) "id" else "groupId", id)
            .put("type", type)
            .put("state", JSONObject(state))

        var index = -1
        for (i in 0 until actionsData.actions.length()) {
            val existingAction = actionsData.actions.getJSONObject(i)
            if (existingAction.getString(if (isDevices) "id" else "groupId") == id) {
                index = i
                break
            }
        }

        if (index != -1) {
            actionsData.actions.put(index, action)
        } else {
            actionsData.actions.put(action)
        }
    }

    fun getActions(): JSONArray {
        return actionsData.actions
    }

    fun clear() {
        actionsData.actions = JSONArray()
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
    val isDevices = SelectedItems.getIsDevices()
    val isSensor = SelectedItems.getIsSensor()

    fun getState(state: MutableMap<String, Boolean>): Map<String, Boolean> {
        val keys = state.keys.toMutableList()

        // Keys should be "on, reverse" instead of "reverse, on"
        // And secondary actions should be omitted for groups
        if (keys.size == 2) {
            keys.add(keys.removeAt(0))
            val newState = mutableMapOf<String, Boolean>()
            newState[keys[0]] = true
            newState[keys[1]] = false
            if (!isDevices) {
                newState.remove(keys[1])
            }
            return newState
        }

        state[keys[0]] = true
        return state
    }

    // Add all actions to a list, or else they would only be added when first shown on the screen.
    // I.e. when the user scrolls though the lazy column
    documents.forEach {
        if (isDevices) {
            Actions.addAction(
                it.id,
                it.get("type") as String,
                getState(it.get("state") as MutableMap<String, Boolean>)
            )
        } else {
            val deviceIds = it.get("devices") as List<String>
            LaunchedEffect(deviceIds) {
                getDocument("devices", deviceIds[0]) { document ->
                    if (document != null) {
                        Actions.addAction(
                            it.id,
                            document.get("type") as String,
                            getState(document.get("state") as MutableMap<String, Boolean>)
                        )
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopTitleBar(
                item = TopTitleBarItem.ChooseActions,
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
                    items(items = documents, key = { item -> item.id }) { document ->
                        ActionCard(document = document)
                    }
                }
            )
        },
        floatingActionButton = {
            CustomFAB(
                icon = Icons.Rounded.ArrowForward,
                onClick = {
                    if (isSensor) {
                        navController.navigate(Finish.route)
                    } else {
                        navController.navigate(ChooseSchedule.route)
                    }
                }
            )
        },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.End
    )
}

@Composable
fun ActionCard(document: DocumentSnapshot) {
    val isDevices = SelectedItems.getIsDevices()
    val isSensor = SelectedItems.getIsSensor()

    val tag = remember { mutableStateOf("") }
    val type = remember { mutableStateOf("") }
    var state = remember { mutableMapOf<String, Boolean>() }
    val keys = remember { mutableListOf<String>() }

    val primaryCheck = remember { mutableStateOf(true) }
    val selectedPrimary = if (primaryCheck.value) LightSteelBlue else FadedLightGrey
    val notSelectedPrimary = if (!primaryCheck.value) LightSteelBlue else FadedLightGrey

    val secondaryCheck = remember { mutableStateOf(true) }
    val selectedSecondary = if (secondaryCheck.value) LightSteelBlue else FadedLightGrey
    val notSelectedSecondary = if (!secondaryCheck.value) LightSteelBlue else FadedLightGrey

    val primaryOn = remember { mutableStateOf("Turn On") }
    val primaryOff = remember { mutableStateOf("Turn Off") }
    val secondaryOn = remember { mutableStateOf("") }
    val secondaryOff = remember { mutableStateOf("") }
    val cardIcon = remember { mutableStateOf(Icons.Filled.BrokenImage) }
    val primaryActionOnIcon = remember { mutableStateOf(Icons.Filled.Power) }
    val primaryActionOffIcon = remember { mutableStateOf(Icons.Filled.PowerOff) }
    val secondaryActionOnIcon = remember { mutableStateOf(Icons.Filled.Power) }
    val secondaryActionOffIcon = remember { mutableStateOf(Icons.Filled.PowerOff) }
    val name = document.get("name") as String

    fun getData(
        tag: String,
        type: String,
        state: MutableMap<String, Boolean>
    ) {
        primaryOn.value = when (type) {
            "toggle" -> "Turn On"
            "openLock" -> "Open"
            else -> "Turn On"
        }

        primaryOff.value = when (type) {
            "toggle" -> "Turn Off"
            "openLock" -> "Close"
            else -> "Turn Off"
        }

        secondaryOn.value = when (type) {
            "openLock" -> "Unlock"
            "fan" -> "Normal"
            else -> ""
        }

        secondaryOff.value = when (type) {
            "openLock" -> "Lock"
            "fan" -> "Reverse"
            else -> ""
        }

        cardIcon.value = when (type) {
            "toggle" -> Icons.Outlined.Lightbulb
            "openLock" -> if (tag == "door") Icons.Outlined.DoorFront else Icons.Outlined.Window
            "screen" -> Icons.Outlined.SmartScreen
            "buzzer" -> Icons.Outlined.SurroundSound
            "sensor" -> Icons.Outlined.Sensors
            "fan" -> Icons.Outlined.RestartAlt
            else -> Icons.Filled.BrokenImage
        }

        primaryActionOnIcon.value = when (type) {
            "openLock" -> if (tag == "door") Icons.Outlined.DoorFront else Icons.Outlined.Window
            else -> Icons.Outlined.Power
        }

        primaryActionOffIcon.value = when (type) {
            "openLock" -> if (tag == "door") Icons.Outlined.DoorFront else Icons.Outlined.Window
            else -> Icons.Outlined.PowerOff
        }

        secondaryActionOnIcon.value = when (type) {
            "openLock" -> Icons.Outlined.LockOpen
            "fan" -> Icons.Outlined.KeyboardDoubleArrowRight
            else -> Icons.Outlined.Power
        }

        secondaryActionOffIcon.value = when (type) {
            "openLock" -> Icons.Outlined.Lock
            "fan" -> Icons.Outlined.KeyboardDoubleArrowLeft
            else -> Icons.Outlined.PowerOff
        }

        keys.clear()
        for (key in state.keys) {
            keys.add(key)
            keys.add(keys.removeAt(0)) // This makes the keys for door [open, locked] instead of [locked, open]
        }
        state[keys[0]] = primaryCheck.value
        state[keys[0]] = primaryCheck.value
        if (keys.size == 2) {
            state[keys[1]] = !secondaryCheck.value // I want reverse and locked to be false initially
        }
    }

    if (isDevices) {
        type.value = document.get("type") as String
        if (type.value == "openLock") {
            tag.value = document.get("tag") as String
        }
        state = document.get("state") as MutableMap<String, Boolean>
        getData(tag.value, type.value, state)
    } else {
        val deviceIds = document.get("devices") as List<String>
        LaunchedEffect(state) {
            getDocument("devices", deviceIds[0]) {
                if (it != null) {
                    type.value = it.get("type") as String
                    if (type.value == "openLock") {
                        tag.value = it.get("tag") as String
                    }
                    state = it.get("state") as MutableMap<String, Boolean>
                    getData(tag.value, type.value, state)
                }
            }
        }
    }
    val id = document.id
    Actions.addAction(id, type.value, state)

    Column(
        Modifier
            .border(
                width = 4.dp,
                color = LightSteelBlue,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .weight(1f)
                    .size(48.dp),
                imageVector = cardIcon.value,
                contentDescription = "$name-icon"
            )
            Text(
                modifier = Modifier.weight(5f),
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
                            text = primaryOff.value,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            modifier = Modifier.weight(2f),
                            imageVector = primaryActionOffIcon.value,
                            contentDescription = "off-icon"
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
                            text = primaryOn.value,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            modifier = Modifier.weight(2f),
                            imageVector = primaryActionOnIcon.value,
                            contentDescription = "on-icon"
                        )
                    }
                }
            }
            if (secondaryOn.value != "" && secondaryOff.value != "" && isDevices) {
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
                                text = secondaryOff.value,
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                modifier = Modifier.weight(2f),
                                imageVector = secondaryActionOffIcon.value,
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
                                text = secondaryOn.value,
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                modifier = Modifier.weight(2f),
                                imageVector = secondaryActionOnIcon.value,
                                contentDescription = "off-icon"
                            )
                        }
                    }
                }
            }
        }
    }
}

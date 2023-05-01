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
import com.HomeApp.ui.composables.ActionCard
import com.HomeApp.ui.composables.Actions
import com.HomeApp.ui.composables.RoutinesFAB
import com.HomeApp.ui.composables.RoutinesTitleBar
import com.HomeApp.ui.composables.RoutinesTitleBarItem
import com.HomeApp.ui.navigation.ChooseSchedule
import com.HomeApp.ui.theme.FadedLightGrey
import com.HomeApp.ui.theme.LightSteelBlue
import com.HomeApp.util.getDocument
import com.google.firebase.firestore.DocumentSnapshot
import org.json.JSONArray
import org.json.JSONObject

@Composable
fun ChooseActionsScreen(
    navController: NavController,
    OnSelfClick: () -> Unit = {}
) {
    Schedule.clearCronString()
    val listHeight = LocalConfiguration.current.screenHeightDp
    val documents = SelectedItems.getItems()
    val isDevices = SelectedItems.getType()[0]

    fun getState(state: MutableMap<String, Boolean>): Map<String, Boolean> {
        val keys = mutableListOf<String>()
        keys.clear()
        for (key in state.keys) {
            keys.add(key)
            keys.add(keys.removeAt(0)) // This makes the keys for door [open, locked] instead of [locked, open]
        }

        state[keys[0]] = true
        if (keys.size == 2) {
            state[keys[1]] = false // I want reverse and locked to be false initially
        }
        Log.d("STATE", state.toString())
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
        }
        else {
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
                    items(items = documents, key = { item -> item.id }) { document ->
                        ActionCard(document = document)
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

package com.HomeApp.screens

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.HomeApp.ui.composables.TopTitleBar
import com.HomeApp.ui.navigation.ChooseType
import com.HomeApp.ui.navigation.Devices
import com.HomeApp.ui.navigation.EditTrigger
import com.HomeApp.ui.theme.FadedLightGrey
import com.HomeApp.ui.theme.LightSteelBlue
import com.google.firebase.firestore.DocumentSnapshot

@Composable
fun TriggersScreen(
    navController: NavController,
    OnSelfClick: () -> Unit = {}
) {
    SelectedItems.setIsSensor(true)

    val listHeight = LocalConfiguration.current.screenHeightDp
    val documents = mutableListOf<DocumentSnapshot>()
    realTimeData!!.triggers.forEach { document ->
        if (document.get("deviceId") == SelectedItems.getSensorId()) {
            documents.add(document)
        }
    }

    Scaffold(
        topBar = {
            TopTitleBar(
                title = "Triggers",
                iconLeft = Icons.Rounded.ArrowBack,
                routeLeftButton = Devices.route,
                iconRight = Icons.Rounded.Add,
                routeRightButton = ChooseType.route,
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
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(items = documents, key = { item -> item.id }) { item ->
                    TriggerCard(triggerItem = item, navController = navController)
                }
            }
        }
    )
}

@Composable
private fun TriggerCard(
    triggerItem: DocumentSnapshot,
    navController: NavController
) {
    val id = triggerItem.id
    val name = triggerItem.get("name") as String
    val description = triggerItem.get("description") as String
    val enabled = triggerItem.get("enabled") as Boolean
    val condition = triggerItem.get("condition") as String
    val value = triggerItem.get("value") as Number
    val resetValue = triggerItem.get("resetValue") as Number

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .border(
                width = 4.dp,
                color = LightSteelBlue,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
            backgroundColor = if (enabled) LightSteelBlue else FadedLightGrey
        ) {
            Row(modifier = Modifier.padding(10.dp)) {
                LazyColumn(
                    modifier = Modifier.weight(3f),
                    content = {
                        item {
                            Text(
                                text = name,
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        item {
                            Text(
                                text = description,
                                fontSize = 15.sp,
                                fontStyle = FontStyle.Normal
                            )
                        }
                    }
                )
                Column(
                    modifier = Modifier
                        .weight(3f)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Condition: ${if (condition == "grt") "Greater" else "Lesser"}")
                    Text(text = "Value: $value")
                    Text(text = "Reset value: $resetValue")
                }
                Column(
                    modifier = Modifier.weight(0.8f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.End
                ) {
                    IconButton(
                        modifier = Modifier
                            .weight(1f)
                            .scale(1.4f),
                        onClick = {
                            SelectedItems.setTriggerId(id)
                            navController.navigate(EditTrigger.route)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "settings-icon"
                        )
                    }
                }

            }
        }
    }
}

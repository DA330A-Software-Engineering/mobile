package com.HomeApp.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Curtains
import androidx.compose.material.icons.filled.DoorFront
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavController
import com.HomeApp.R
import com.HomeApp.screens.DevicesDummy

@Composable
fun DeviceCard(
    navController: NavController,
    modifier: Modifier = Modifier,
    deviceItem: DevicesDummy
) {

    val cardIcon: ImageVector = when (deviceItem.type){
        "light" -> Icons.Filled.Lightbulb
        "door"-> Icons.Filled.DoorFront
        "curtain" -> Icons.Filled.Curtains
        else -> Icons.Filled.BrokenImage
    }

    val deviceState: String = when(deviceItem.type) {
        "light" -> if (deviceItem.state.getString("on") == "true") "On" else "Off"
        "door"
    }

    Row(modifier = Modifier
        .fillMaxWidth()
        .background(colorResource(id = R.color.LightSteelBlue))) {

        Icon(imageVector = cardIcon, contentDescription ="")
        Text(text = deviceItem.name)
        Text(text = if (deviceItem.type== "door") deviceItem.state.getString("open") else if (deviceItem.type== "curtain") deviceItem.state.getString("open") else deviceItem.state.getString("on"))
    }

}
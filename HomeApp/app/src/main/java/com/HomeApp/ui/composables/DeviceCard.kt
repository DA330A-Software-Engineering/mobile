package com.HomeApp.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
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
        is "light" ->
    }

    Row(modifier = Modifier
        .fillMaxWidth()
        .background(colorResource(id = R.color.LightSteelBlue))) {

        Icon(imageVector = if (), contentDescription ="")
    }

}
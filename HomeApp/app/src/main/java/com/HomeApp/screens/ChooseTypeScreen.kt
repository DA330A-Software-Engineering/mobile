package com.HomeApp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Devices
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.HomeApp.ui.composables.RoutinesTitleBar
import com.HomeApp.ui.composables.RoutinesTitleBarItem
import com.HomeApp.ui.composables.TitledDivider
import com.HomeApp.ui.navigation.ChooseDevices
import com.HomeApp.ui.navigation.ChooseGroups
import com.HomeApp.ui.theme.LightSteelBlue

@Composable
fun ChooseTypeScreen(
    navController: NavController,
    OnSelfClick: () -> Unit = {}
) {
    SelectedItems.clearItems()
    val listHeight = LocalConfiguration.current.screenHeightDp

    Scaffold(
        topBar = {
            RoutinesTitleBar(
                item = RoutinesTitleBarItem.ChooseType,
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
                    item {
                        DeviceType(
                            title = "Device",
                            text = "Choose from all devices",
                            icon = Icons.Rounded.Devices,
                            onClick = { navController.navigate(ChooseDevices.route) }
                        )
                    }
                    item {
                        DeviceType(
                            title = "Group",
                            text = "Choose from all groups",
                            icon = Icons.Rounded.Groups,
                            onClick = { navController.navigate(ChooseGroups.route) }
                        )
                    }
                }
            )
        }
    )
}

@Composable
private fun DeviceType(
    title: String,
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clickable(onClick = onClick),
        backgroundColor = LightSteelBlue,
    ) {
        Row(
            Modifier.padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                modifier = Modifier
                    .scale(2.2f)
                    .weight(1f),
                imageVector = icon,
                contentDescription = "$title-icon"
            )
            Column(Modifier.weight(2f)) {
                Text(
                    text = title,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = text,
                    fontSize = 15.sp
                )
            }
            Icon(
                modifier = Modifier
                    .scale(2.2f)
                    .weight(1f),
                imageVector = Icons.Rounded.ArrowForward,
                contentDescription = "arrow-icon"
            )
        }
    }
}
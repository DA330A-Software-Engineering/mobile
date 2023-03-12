package com.HomeApp.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Curtains
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.outlined.DoorFront
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.People
import androidx.compose.ui.graphics.vector.ImageVector

val microphoneIcon: ImageVector = Icons.Rounded.Mic

enum class SideBarOptions(val title: String, val icon: ImageVector?, val route: String) {
    PROFILE("Profile & Family", Icons.Rounded.People, ""),
    NOTIFICATIONS("Notifications", Icons.Rounded.Notifications, ""),
    History("History", null, ""),
    LOGOUT("Logout", null, ""),
}

val enableTopDrawer: Boolean = false

enum class DevicesFilters(val filterName: String, val filterIcon: ImageVector, val filterValue: String) {
    LIGHTS(filterName = "Lights", filterIcon = Icons.Filled.Lightbulb, filterValue = "lights"),
    CURTAINS(filterName = "Curtains", filterIcon = Icons.Filled.Curtains, filterValue = "curtains"),
    DOORS(filterName = "Doors", filterIcon = Icons.Outlined.DoorFront, filterValue = "doors")
}


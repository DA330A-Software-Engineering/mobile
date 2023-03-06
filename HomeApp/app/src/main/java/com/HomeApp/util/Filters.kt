package com.HomeApp.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Curtains
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.outlined.DoorFront
import androidx.compose.ui.graphics.vector.ImageVector

enum class DevicesFilters(val filterName: String, val filterIcon: ImageVector, val filterValue: String) {
    LIGHTS(filterName = "Lights", filterIcon = Icons.Filled.Lightbulb, filterValue = "lights"),
    CURTAINS(filterName = "Curtains", filterIcon = Icons.Filled.Curtains, filterValue = "curtains"),
    DOORS(filterName = "Doors", filterIcon = Icons.Outlined.DoorFront, filterValue = "doors")
}

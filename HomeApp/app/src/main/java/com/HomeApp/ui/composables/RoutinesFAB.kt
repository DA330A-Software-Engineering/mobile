package com.HomeApp.ui.composables

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.HomeApp.ui.theme.GhostWhite

@Composable
fun RoutinesFAB(
    icon: ImageVector,
    onClick: () -> Unit
) {
    FloatingActionButton(
        modifier = Modifier.scale(1f),
        onClick = onClick,
        backgroundColor = GhostWhite,
        content = {
            Icon(
                modifier = Modifier.scale(1.4f),
                imageVector = icon,
                contentDescription = icon.name,
                tint = Color.Black
            )
        }
    )
}
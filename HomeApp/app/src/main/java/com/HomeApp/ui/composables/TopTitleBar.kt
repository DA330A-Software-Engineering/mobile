package com.HomeApp.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.HomeApp.screens.SelectedItems

@Composable
fun TopTitleBar(
    title: String,
    iconLeft: ImageVector,
    routeLeftButton: String,
    iconRight: ImageVector?,
    routeRightButton: String?,
    navController: NavController
) {
    val showDialog = remember { mutableStateOf(false) }
    if (showDialog.value) {
        Dialog(
            showDialog = showDialog,
            navController = navController,
            route = routeRightButton!!
        )
    }

    Column(modifier = Modifier) {
        Spacer(modifier = Modifier.height(30.dp))
        Row(
            modifier = Modifier.height(60.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate(routeLeftButton) }
            ) {
                Icon(
                    modifier = Modifier.scale(2.2f),
                    imageVector = iconLeft,
                    contentDescription = "left-icon"
                )
            }
            Text(
                text = title,
                modifier = Modifier.weight(2f),
                fontSize = 35.sp,
                textAlign = TextAlign.Center
            )
            IconButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    if (iconRight == Icons.Rounded.Add) {
                        SelectedItems.setIsEdit(false)
                    }
                    navController.navigate(routeRightButton!!)
                }
            ) {
                if (iconRight != null) {
                    Icon(
                        modifier = Modifier.scale(2.2f),
                        imageVector = iconRight,
                        contentDescription = "right-icon"
                    )
                } else {
                    Box(modifier = Modifier.scale(2.2f))
                }
            }
        }
    }
}

@Composable
private fun Dialog(
    showDialog: MutableState<Boolean>,
    navController: NavController,
    route: String
) {
    AlertDialog(
        onDismissRequest = { showDialog.value = false },
        title = {
            Column {
                Text(
                    text = "Are you sure you want to exit?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(text = "All progress will be lost", fontSize = 15.sp)
            }
        },
        confirmButton = {
            TextButton(onClick = { navController.navigate(route) }) {
                Text(text = "Exit")
            }
        },
        dismissButton = {
            TextButton(onClick = { showDialog.value = false }) {
                Text(text = "Cancel")
            }
        }
    )
}
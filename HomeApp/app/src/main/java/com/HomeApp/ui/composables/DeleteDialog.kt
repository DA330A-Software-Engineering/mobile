package com.HomeApp.ui.composables

import android.util.Log
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import com.HomeApp.screens.SelectedItems
import com.HomeApp.ui.navigation.Routines
import com.HomeApp.ui.navigation.Triggers
import com.HomeApp.ui.theme.DarkRed
import com.HomeApp.util.ApiConnector
import com.HomeApp.util.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun DeleteDialog(
    deleteDialog: MutableState<Boolean>,
    name: String,
    token: String,
    id: String,
    navController: NavController
) {
    val coroutine = rememberCoroutineScope()
    val onRespond: (ApiResult) -> Unit = {
        Log.d("DELETE", it.toString())
    }

    AlertDialog(
        title = { Text(text = "Delete $name") },
        text = { Text(text = "Are you sure you want to delete $name?") },
        onDismissRequest = { deleteDialog.value = false },
        confirmButton = {
            TextButton(
                onClick = {
                    deleteDialog.value = false
                    if (SelectedItems.getIsSensor()) {
                        coroutine.launch(Dispatchers.IO) {
                            ApiConnector.deleteTrigger(
                                token = token,
                                triggerId = id,
                                onRespond = onRespond
                            )
                        }
                        navController.navigate(Triggers.route)
                    } else {
                        coroutine.launch(Dispatchers.IO) {
                            ApiConnector.deleteRoutine(
                                token = token,
                                id = id,
                                onRespond = onRespond
                            )
                        }
                        navController.navigate(Routines.route)
                    }
                }
            ) {
                Text(text = "Delete", color = DarkRed)
            }
        },
        dismissButton = {
            TextButton(onClick = { deleteDialog.value = false }) {
                Text(text = "Cancel")
            }
        }
    )
}
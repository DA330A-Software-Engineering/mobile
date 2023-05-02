package com.HomeApp.ui.composables

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import com.HomeApp.screens.SelectedItems
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
    onRespond: (result: ApiResult) -> Unit
) {
    val coroutine = rememberCoroutineScope()

    AlertDialog(
        title = { Text(text = "Delete $name") },
        text = { Text(text = "Are you sure you want to delete $name?") },
        onDismissRequest = { deleteDialog.value = false },
        confirmButton = {
            TextButton(
                onClick = {
                    deleteDialog.value = false
                    coroutine.launch(Dispatchers.IO) {
                        if (SelectedItems.getIsSensor()) {
                            ApiConnector.deleteTrigger(
                                token = token,
                                triggerId = id,
                                onRespond = onRespond
                            )
                        } else {
                            ApiConnector.deleteRoutine(
                                token = token,
                                id = id,
                                onRespond = onRespond
                            )
                        }
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
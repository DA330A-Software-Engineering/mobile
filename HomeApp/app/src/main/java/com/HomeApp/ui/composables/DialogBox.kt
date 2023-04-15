package com.HomeApp.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.DropdownMenu
import com.HomeApp.screens.Devices
import com.HomeApp.screens.rememberFirestoreCollections
import com.HomeApp.util.days
import com.HomeApp.util.months

@Composable
fun DialogBox(showDialog: MutableState<Boolean>) {
    val name = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { showDialog.value = false },
        title = { Text("New routine") },
        text = {
            Column {
                TextField(
                    value = name.value,
                    onValueChange = { name.value = it },
                    label = { Text(text = "Name") }
                )
                TextField(
                    modifier = Modifier.padding(top = 8.dp),
                    value = description.value,
                    onValueChange = { description.value = it },
                    label = { Text(text = "Description") }
                )
                Row(
                    Modifier.fillMaxWidth().wrapContentHeight(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomDropdownMenu(schedule = Schedule.Device)
                    CustomDropdownMenu(schedule = Schedule.DayOfWeek)
                }
                Row(
                    Modifier.fillMaxWidth().wrapContentHeight(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomDropdownMenu(schedule = Schedule.Hour)
                    CustomDropdownMenu(schedule = Schedule.Minute)
                }
                Row(
                    Modifier.fillMaxWidth().wrapContentHeight(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomDropdownMenu(schedule = Schedule.Date)
                    CustomDropdownMenu(schedule = Schedule.Month)
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    // TODO: Handle confirm button press here
                    showDialog.value = false
                }
            ) {
                Text("Done")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { showDialog.value = false }
            ) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun CustomDropdownMenu(schedule: Schedule) {
    var selection by remember { mutableStateOf(schedule.initValue) }
    var expanded by remember { mutableStateOf(false) }
    val documents = rememberFirestoreCollections("devices", Devices::class.java)
    val deviceNames = mutableListOf<String>()
    documents.forEach { item ->
        val deviceName = item.getString("name")
        if (deviceName != null) {
            deviceNames.add(deviceName)
        }
    }

    Column {
        Row(
            Modifier.width(130.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = schedule.text)
            Spacer(modifier = Modifier.width(3.dp))
            Text(text = selection)
            IconButton(onClick = { expanded = true }) {
                Icon(
                    imageVector = Icons.Rounded.ExpandMore,
                    contentDescription = "expand-icon"
                )
            }
        }
        DropdownMenu(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            expanded = expanded,
            onDismissRequest = { expanded = false },
            content = {
                Column(
                    Modifier
                        .height(230.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (schedule) {
                        Schedule.Minute -> {
                            DropdownMenuItem(
                                selection = selection,
                                options = (0..59).map { it.toString() },
                                onItemClick = {
                                    selection = it
                                    expanded = false
                                }
                            )
                        }
                        Schedule.Hour -> {
                            DropdownMenuItem(
                                selection = selection,
                                options = (0..23).map { it.toString() },
                                onItemClick = {
                                    selection = it
                                    expanded = false
                                }
                            )
                        }
                        Schedule.Date -> {
                            DropdownMenuItem(
                                selection = selection,
                                options = listOf("Any") + (0..31).map { it.toString() },
                                onItemClick = {
                                    selection = it
                                    expanded = false
                                }
                            )
                        }
                        Schedule.Month -> {
                            DropdownMenuItem(
                                selection = selection,
                                options = months,
                                onItemClick = {
                                    selection = it
                                    expanded = false
                                }
                            )
                        }
                        Schedule.DayOfWeek -> {
                            DropdownMenuItem(
                                selection = selection,
                                options = days,
                                onItemClick = {
                                    selection = it
                                    expanded = false
                                }
                            )
                        }
                        Schedule.Device -> {
                            DropdownMenuItem(
                                selection = selection,
                                options = deviceNames,
                                onItemClick = {
                                    selection = it
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        )
    }
}

@Composable
private fun DropdownMenuItem(
    selection: String,
    options: List<String>,
    onItemClick: (String) -> Unit
) {
    options.forEach { option ->
        DropdownMenuItem(
            onClick = {
                onItemClick(option)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = option)
        }
    }
}

sealed class Schedule(
    val text: String,
    val initValue: String
) {
    object Minute : Schedule(
        text = "Minute:",
        initValue = "0",
    )

    object Hour : Schedule(
        text = "Hour:",
        initValue = "0"
    )

    object Date : Schedule(
        text = "Date:",
        initValue = "Any"
    )

    object Month : Schedule(
        text = "Month:",
        initValue = "Any"
    )

    object DayOfWeek : Schedule(
        text = "Day:",
        initValue = "Any"
    )
    
    object Device : Schedule(
        text = "Device",
        initValue = ""
    )
}


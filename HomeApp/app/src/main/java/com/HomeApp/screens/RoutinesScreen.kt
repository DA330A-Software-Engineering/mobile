package com.HomeApp.screens

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.HomeApp.realTimeData
import com.HomeApp.ui.composables.RoutinesTitleBar
import com.HomeApp.ui.composables.RoutinesTitleBarItem
import com.HomeApp.ui.composables.TitledDivider
import com.HomeApp.ui.theme.DarkRed
import com.HomeApp.ui.theme.FadedLightGrey
import com.HomeApp.ui.theme.GhostWhite
import com.HomeApp.ui.theme.LightSteelBlue
import com.HomeApp.ui.theme.LighterGray
import com.HomeApp.util.ApiConnector
import com.HomeApp.util.ApiResult
import com.HomeApp.util.DayFilters
import com.HomeApp.util.LocalStorage
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray

data class Routines(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val schedule: String = "",
    val enabled: Boolean = false,
    val repeatable: Boolean = false,
    val actionList: JSONArray = JSONArray()
)

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun RoutinesScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    OnSelfClick: () -> Unit = {}
) {
    val listHeight = LocalConfiguration.current.screenHeightDp
    val documents = realTimeData!!.routines

    Scaffold(
        topBar = {
            RoutinesTitleBar(item = RoutinesTitleBarItem.Routines, navController = navController)
        },
        content = {
            LazyColumn(
                modifier = Modifier
                    .height(listHeight.dp)
                    .padding(it)
                    .padding(vertical = 10.dp)
                    .padding(top = 10.dp)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(items = documents, key = { item -> item.id }) { item ->
                    RoutineCard(routineItem = item)
                }
            }
        }
    )
}

@Composable
private fun RoutineCard(routineItem: DocumentSnapshot) {
    val focusManager: FocusManager = LocalFocusManager.current
    val context = LocalContext.current
    val token = LocalStorage.getToken(context)
    val coroutine = rememberCoroutineScope()

    val id = routineItem.id
    val name = remember { mutableStateOf(routineItem.get("name") as String) }
    val description = remember { mutableStateOf(routineItem.get("description") as String) }
    val schedule = remember { mutableStateOf(routineItem.get("schedule") as String) }
    val enabled = remember { mutableStateOf(routineItem.get("enabled") as Boolean) }
    val repeatable = remember { mutableStateOf(routineItem.get("repeatable") as Boolean) }

    val cron = schedule.value.split(" ")
    val minute = remember { mutableStateOf(cron[1]) }
    val hour = remember { mutableStateOf(cron[2]) }
    val days = remember { mutableStateOf(cron[5]) }

    val onRespond: (ApiResult) -> Unit = {
        Log.d("RESPOND", it.toString())
    }

    val deleteDialog = remember { mutableStateOf(false) }
    if (deleteDialog.value) {
        DeleteDialog(
            deleteDialog = deleteDialog,
            name = name.value,
            token = token,
            id = id,
            onRespond = onRespond
        )
    }

    fun updateRoutine() {
        coroutine.launch(Dispatchers.IO) {
            ApiConnector.updateRoutine(
                token = token,
                id = id,
                name = name.value,
                description = description.value,
                schedule = schedule.value,
                enabled = enabled.value,
                repeatable = repeatable.value,
                onRespond = onRespond
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .border(
                width = 4.dp,
                color = LightSteelBlue,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DayFilters.values().forEach {
                IconButton(
                    modifier = Modifier
                        .background(
                            if (days.value.contains(it.cron)) {
                                LightSteelBlue
                            } else {
                                FadedLightGrey
                            },
                            CircleShape
                        ),
                    onClick = {
                        val selectedDays = days.value.split(",").toMutableList()
                        if (selectedDays.contains(it.cron)) {
                            selectedDays.remove(it.cron)
                        } else {
                            selectedDays.add(it.cron)
                        }
                        val sortedList = selectedDays.sortedBy { day -> day.toInt() }
                        days.value = sortedList.joinToString(separator = ",")
                        val fields = schedule.value.split(" ")
                        schedule.value =
                            "${fields[0]} ${fields[1]} ${fields[2]} ${fields[3]} ${fields[4]} ${days.value}"
                        updateRoutine()
                    }
                ) {
                    Text(
                        modifier = Modifier.scale(1.6f),
                        text = it.letter,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }
        Card(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = {
                    enabled.value = !enabled.value
                    updateRoutine()
                }),
            backgroundColor = if (enabled.value) LightSteelBlue else FadedLightGrey
        ) {
            Row(modifier = Modifier.padding(10.dp)) {
                LazyColumn(
                    modifier = Modifier.weight(2f),
                    content = {
                        item {
                            InputText(
                                text = name,
                                isTitle = true,
                                enabled = enabled.value,
                                onDone = {
                                    if (name.value != "") {
                                        focusManager.clearFocus()
                                        updateRoutine()
                                    } else {
                                        val message = "Please enter a name for the routine"
                                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                    }
                                }
                            )
                        }
                        item {
                            InputText(
                                text = description,
                                isTitle = false,
                                enabled = enabled.value,
                                onDone = {
                                    if (description.value != "") {
                                        focusManager.clearFocus()
                                        updateRoutine()
                                    } else {
                                        val message = "Please enter a name for the routine"
                                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                    }
                                }
                            )
                        }
                    }
                )
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    Row(modifier = Modifier.weight(1f)) {
                        DisplayTime(
                            time = hour,
                            isHour = true,
                            schedule = schedule,
                            updateRoutine = { updateRoutine() }
                        )
                        Text(text = ":", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                        DisplayTime(
                            time = minute,
                            isHour = false,
                            schedule = schedule,
                            updateRoutine = { updateRoutine() }
                        )
                    }
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(
                            modifier = Modifier.scale(1.4f),
                            onClick = {
                                repeatable.value = !repeatable.value
                                updateRoutine()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Recycling,
                                contentDescription = "repeatable-icon",
                                tint = if (enabled.value) {
                                    if (repeatable.value) GhostWhite else FadedLightGrey
                                } else {
                                    if (repeatable.value) Color.Black else LighterGray
                                }
                            )
                        }
                        IconButton(
                            modifier = Modifier.scale(1.4f),
                            onClick = { deleteDialog.value = true }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "delete-icon",
                                tint = DarkRed
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DeleteDialog(
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
                        ApiConnector.deleteRoutine(
                            token = token,
                            id = id,
                            onRespond = onRespond
                        )
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

@Composable
private fun InputText(
    text: MutableState<String>,
    isTitle: Boolean,
    enabled: Boolean,
    onDone: (KeyboardActionScope.() -> Unit)?
) {
    BasicTextField(
        value = text.value,
        onValueChange = { text.value = it },
        textStyle = TextStyle(
            fontSize = if (isTitle) 25.sp else 15.sp,
            fontWeight = if (isTitle) FontWeight.Bold else FontWeight.Normal,
            color = if (enabled) GhostWhite else Color.Black
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = onDone)
    )
}

@Composable
private fun DisplayTime(
    time: MutableState<String>,
    isHour: Boolean,
    schedule: MutableState<String>,
    updateRoutine: () -> Unit
) {
    val expanded = remember { mutableStateOf(false) }

    Text(
        modifier = Modifier.clickable(onClick = { expanded.value = true }),
        text = if (time.value.length == 2) time.value else "0${time.value}",
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold
    )
    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false },
        content = {
            Column(
                modifier = Modifier
                    .height(150.dp)
                    .width(80.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                for (i in 0..if (isHour) 23 else 59) {
                    DropdownMenuItem(onClick = {
                        time.value = "$i"
                        expanded.value = false
                        val fields = schedule.value.split(" ")
                        if (isHour) {
                            schedule.value =
                                "${fields[0]} ${fields[1]} ${time.value} ${fields[3]} ${fields[4]} ${fields[5]}"
                        } else {
                            schedule.value =
                                "${fields[0]} ${time.value} ${fields[2]} ${fields[3]} ${fields[4]} ${fields[5]}"
                        }
                        updateRoutine()
                    }) {
                        Text(
                            text = if ("$i".length == 2) "$i" else "0$i",
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    )
}

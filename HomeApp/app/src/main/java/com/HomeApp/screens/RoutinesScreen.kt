package com.HomeApp.screens

import android.util.Log
import android.widget.Toast
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
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.HomeApp.ui.composables.DeleteDialog
import com.HomeApp.ui.composables.TitledDivider
import com.HomeApp.ui.composables.TopTitleBar
import com.HomeApp.ui.navigation.ChooseType
import com.HomeApp.ui.navigation.Home
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

@Composable
fun RoutinesScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    OnSelfClick: () -> Unit = {}
) {
    SelectedItems.setIsSensor(false)

    val listHeight = LocalConfiguration.current.screenHeightDp
    val documents = realTimeData!!.routines
    val filteredDocuments = remember(documents) {
        mutableStateListOf(*documents.toTypedArray())
    }
    filteredDocuments.clear()
    filteredDocuments.addAll(documents)

    Scaffold(
        topBar = {
            TopTitleBar(
                title = "Routines",
                iconLeft = Icons.Rounded.ArrowBack,
                routeLeftButton = Home.route,
                iconRight = Icons.Rounded.Add,
                routeRightButton = ChooseType.route,
                navController = navController
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .height(listHeight.dp)
                    .padding(it)
                    .padding(vertical = 10.dp)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TitledDivider(navController = navController, title = "Filters")
                DayIcons(
                    documents = documents,
                    onFilterChanged = { filteredList ->
                        filteredDocuments.clear()
                        filteredDocuments.addAll(filteredList)
                    }
                )
                LazyColumn(
                    modifier = Modifier.padding(top = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(items = filteredDocuments, key = { item -> item.id }) { item ->
                        RoutineCard(routineItem = item, navController = navController)
                    }
                }
            }
        }
    )
}

@Composable
private fun DayIcons(
    documents: List<DocumentSnapshot>,
    onFilterChanged: (List<DocumentSnapshot>) -> Unit
) {
    val selectedDays = remember {
        mutableStateMapOf<DayFilters, Boolean>().apply {
            putAll(DayFilters.values().associateWith { true })
        }
    }

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        DayFilters.values().forEach { dayFilter ->
            val selected = selectedDays[dayFilter] ?: true
            IconButton(
                modifier = Modifier
                    .scale(0.9f)
                    .background(
                        if (selected) LightSteelBlue else FadedLightGrey,
                        CircleShape
                    ),
                onClick = {
                    selectedDays[dayFilter] = !selected
                    val selectedDayValues = selectedDays
                        .filter { it.value }
                        .map { it.key.cron }
                    val filteredList = documents.filter { document ->
                        val cronString = document["schedule"] as String
                        val cronDays = cronString.split(" ").last()
                        selectedDayValues.any { it in cronDays.split(",") }
                    }
                    onFilterChanged(filteredList)
                }
            ) {
                Text(
                    modifier = Modifier.scale(1.6f),
                    text = dayFilter.letter,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun RoutineCard(
    routineItem: DocumentSnapshot,
    navController: NavController
) {
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
            navController = navController
        )
    }

    val settingsMenu = remember { mutableStateOf(false) }

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
                actions = Actions.getActions(),
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
                        .scale(0.8f)
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
                        modifier = Modifier.scale(1.5f),
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
                                    if (name.value.isBlank() || name.value.isEmpty()) {
                                        val message = "Please enter a name for the routine"
                                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                    } else {
                                        focusManager.clearFocus()
                                        updateRoutine()
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
                                    if (description.value.isBlank() || description.value.isEmpty()) {
                                        val message = "Please enter a name for the routine"
                                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                    } else {
                                        focusManager.clearFocus()
                                        updateRoutine()
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
                            onClick = { settingsMenu.value = true }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Settings,
                                contentDescription = "settings-icon"
                            )
                            SettingsDropdownMenu(
                                id = id,
                                expanded = settingsMenu,
                                deleteDialog = deleteDialog,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
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
                    DropdownMenuItem(
                        onClick = {
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
                        }
                    ) {
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

@Composable
private fun SettingsDropdownMenu(
    id: String,
    expanded: MutableState<Boolean>,
    deleteDialog: MutableState<Boolean>,
    navController: NavController
) {
    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false },
        content = {
            DropdownMenuItem(
                onClick = {
                    SelectedItems.setRoutineId(id)
                    SelectedItems.setIsEdit(true)
                    navController.navigate(ChooseType.route)
                }
            ) {
                Text(text = "Edit Actions")
            }
            DropdownMenuItem(
                onClick = {
                    expanded.value = false
                    deleteDialog.value = true
                }
            ) {
                Text(text = "Delete", color = DarkRed)
            }
        }
    )
}

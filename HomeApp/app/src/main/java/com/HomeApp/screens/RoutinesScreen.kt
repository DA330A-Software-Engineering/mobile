package com.HomeApp.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.HomeApp.ui.composables.RoutinesTitleBar
import com.HomeApp.ui.composables.RoutinesTitleBarItem
import com.HomeApp.ui.composables.TitledDivider
import com.HomeApp.ui.theme.FadedLightGrey
import com.HomeApp.ui.theme.GhostWhite
import com.HomeApp.ui.theme.LightSteelBlue
import com.HomeApp.util.ApiConnector
import com.HomeApp.util.ApiResult
import com.HomeApp.util.DayFilters
import com.HomeApp.util.LocalStorage
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
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

@Composable
fun RoutinesScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    OnSelfClick: () -> Unit = {}
) {
    val coroutine = rememberCoroutineScope()
    val listHeight = LocalConfiguration.current.screenHeightDp
    val documents = rememberFirestoreCollection("routines", Routines::class.java)
    Log.d("Documents", documents.toString())

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
                item { TitledDivider(navController = navController, title = "Filters") }
                item { DayIcons() }
                items(items = documents, key = { item -> item.id }) { item ->
                    RoutineCard(routineItem = item)
                }
            }
        }
    )
}

@Composable
private fun DayIcons() {
    val selectedColor = LightSteelBlue
    val notSelectedColor = FadedLightGrey
    val selectionState = remember {
        mutableStateMapOf<DayFilters, Boolean>().apply {
            putAll(DayFilters.values().associateWith { false })
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        DayFilters.values().forEach {
            val selected = selectionState[it] ?: false

            IconButton(
                modifier = Modifier
                    .background(
                        if (selected) selectedColor else notSelectedColor,
                        CircleShape
                    ),
                onClick = { selectionState[it] = !selected }
            ) {
                Text(
                    modifier = Modifier.scale(1.6f),
                    text = it.letter,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun RoutineCard(
    routineItem: DocumentSnapshot
) {
    val context = LocalContext.current
    val token = LocalStorage.getToken(context)
    val coroutine = rememberCoroutineScope()

    val id = routineItem.id
    val name = remember { mutableStateOf(routineItem.get("name") as String) }
    val description = remember { mutableStateOf(routineItem.get("description") as String) }
    val schedule = routineItem.get("schedule") as String
    val enabled = routineItem.get("enabled") as Boolean
    val repeatable = routineItem.get("repeatable") as Boolean

    val cron = schedule.split(" ")
    val minute = cron[0]
    val hour = cron[1]
    val dates = cron[2]
    val months = cron[3]
    val days = cron[4]

    val selectedDays = remember { mutableListOf<String>() }

    if (days == "*") {
        DayFilters.values().toList().forEach {
            selectedDays.add(it.cron.toString())
        }
    } else {
        DayFilters.values().toList().forEach {
            val number = it.cron.toString()
            if (days.contains(number)) {
                selectedDays.add(number)
            }
        }
    }

    val onRespond: (ApiResult) -> Unit = {
        Log.d("RESPOND", it.toString())
    }

    val cardColor = if (enabled) LightSteelBlue else FadedLightGrey


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
                            if (selectedDays.contains(it.cron.toString())) {
                                LightSteelBlue
                            } else {
                                FadedLightGrey
                            },
                            CircleShape
                        ),
                    onClick = {},
                    enabled = false
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
            modifier = Modifier.fillMaxSize(),
            backgroundColor = cardColor
        ) {
            Row(modifier = Modifier.padding(10.dp)) {
                LazyColumn(
                    modifier = Modifier.weight(2f),
                    content = {
                        item {
                            InputText(
                                text = name,
                                enabled = enabled,
                                onValueChange = {
                                    name.value = it
                                    coroutine.launch(Dispatchers.IO) {
                                        ApiConnector.updateRoutine(
                                            token = token,
                                            id = id,
                                            name = name.value,
                                            description = description.value,
                                            schedule = schedule,
                                            enabled = enabled,
                                            repeatable = repeatable,
                                            onRespond = onRespond
                                        )
                                    }
                                }
                            )
                        }
                        item {
                            InputText(
                                text = name,
                                enabled = enabled,
                                onValueChange = {
                                    description.value = it
                                    coroutine.launch(Dispatchers.IO) {
                                        ApiConnector.updateRoutine(
                                            token = token,
                                            id = id,
                                            name = name.value,
                                            description = description.value,
                                            schedule = schedule,
                                            enabled = enabled,
                                            repeatable = repeatable,
                                            onRespond = onRespond
                                        )
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
                        Text(
                            text = "$hour:",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = minute,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (repeatable) {
                        IconButton(
                            modifier = Modifier
                                .weight(1f)
                                .scale(1.6f),
                            onClick = {},
                            enabled = false
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Recycling,
                                contentDescription = "repeatable",
                                tint = if (enabled) GhostWhite else Color.Black
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
    enabled: Boolean,
    onValueChange: (String) -> Unit
) {
    val focusManager: FocusManager = LocalFocusManager.current

    BasicTextField(
        value = text.value,
        onValueChange = onValueChange,
        textStyle = TextStyle(
            fontSize = 15.sp,
            color = if (enabled) GhostWhite else Color.Black
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
    )
}
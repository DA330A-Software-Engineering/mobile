package com.HomeApp.screens

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.HomeApp.ui.composables.RoutinesFAB
import com.HomeApp.ui.composables.TopTitleBar
import com.HomeApp.ui.composables.TopTitleBarItem
import com.HomeApp.ui.navigation.Routines
import com.HomeApp.ui.navigation.Triggers
import com.HomeApp.ui.theme.FadedLightGrey
import com.HomeApp.ui.theme.LightSteelBlue
import com.HomeApp.util.ApiConnector
import com.HomeApp.util.ApiResult
import com.HomeApp.util.LocalStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun FinishScreen(
    navController: NavController,
    OnSelfClick: () -> Unit = {}
) {
    val coroutine = rememberCoroutineScope()
    val context = LocalContext.current
    val token = LocalStorage.getToken(context)
    val listHeight = LocalConfiguration.current.screenHeightDp
    val cronString = Schedule.getCronString()

    val isDevices = SelectedItems.getIsDevices()
    val isSensor = SelectedItems.getIsSensor()

    val routineName = remember { mutableStateOf("") }
    val routineDescription = remember { mutableStateOf("") }

    val condition = remember { mutableStateOf(true) }
    val conditionValue = remember { mutableStateOf(if (condition.value) "grt" else "lsr") }
    val value = remember { mutableStateOf("0") }
    val resetValue = remember { mutableStateOf("0") }
    val enabled = remember { mutableStateOf(true) }
    val repeatable = remember { mutableStateOf(true) }

    val onRespond: (ApiResult) -> Unit = {
        Log.d("RESPOND", it.toString())
    }

    val resetValueFocusRequester = FocusRequester()
    val descriptionFocusRequester = FocusRequester()
    val focusManager: FocusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopTitleBar(
                item = TopTitleBarItem.Finish,
                navController = navController
            )
        },
        content = { it ->
            LazyColumn(
                modifier = Modifier
                    .height(listHeight.dp)
                    .fillMaxWidth()
                    .padding(it)
                    .padding(vertical = 10.dp, horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    item {
                        Text(
                            text = "Choose name and description",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(15.dp))
                        InputText(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            text = routineName,
                            label = "Name",
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Text,
                            keyboardActions = KeyboardActions(
                                onNext = { descriptionFocusRequester.requestFocus() }
                            )
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(15.dp))
                        InputText(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                                .focusRequester(descriptionFocusRequester),
                            text = routineDescription,
                            label = "Description",
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Text,
                            keyboardActions = KeyboardActions(
                                onDone = { focusManager.clearFocus() }
                            )
                        )
                    }
                    if (isSensor) {
                        item {
                            Divider(
                                modifier = Modifier
                                    .padding(horizontal = 5.dp)
                                    .padding(vertical = 20.dp),
                                color = FadedLightGrey,
                                thickness = 2.dp
                            )
                            Text(
                                text = "Choose when the actions should execute",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(15.dp))
                            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                                InputText(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(horizontal = 30.dp),
                                    text = value,
                                    label = "Value",
                                    imeAction = ImeAction.Next,
                                    keyboardType = KeyboardType.Number,
                                    keyboardActions = KeyboardActions(
                                        onNext = {
                                            val newValue = value.value.toIntOrNull()
                                            if (newValue == null) {
                                                value.value = "0"
                                            } else if (newValue >= 1023) {
                                                value.value = "1023"
                                            }
                                            resetValueFocusRequester.requestFocus()
                                        }
                                    )
                                )
                                InputText(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(horizontal = 30.dp)
                                        .focusRequester(resetValueFocusRequester),
                                    text = resetValue,
                                    label = "Reset value",
                                    imeAction = ImeAction.Done,
                                    keyboardType = KeyboardType.Number,
                                    keyboardActions = KeyboardActions(
                                        onDone = {
                                            val newValue = resetValue.value.toIntOrNull()
                                            if (newValue == null) {
                                                value.value = "0"
                                            } else if (newValue >= 1023) {
                                                resetValue.value = "1023"
                                            }
                                            focusManager.clearFocus()
                                        }
                                    )
                                )
                            }
                        }
                        item { SwitchCard(title = "Condition", check = condition) }
                    }
                    item { SwitchCard(title = "Enabled", check = enabled) }
                    if (!isSensor) {
                        item {
                            SwitchCard(
                                title = "Repeatable",
                                check = repeatable
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            RoutinesFAB(
                icon = Icons.Rounded.Done,
                onClick = {
                    if (isSensor) {
                        coroutine.launch(Dispatchers.IO) {
                            ApiConnector.createTrigger(
                                token = token,
                                deviceId = SelectedItems.getSensorId(),
                                name = routineName.value,
                                description = routineDescription.value,
                                condition = conditionValue.value,
                                value = value.value.toInt(),
                                resetValue = resetValue.value.toInt(),
                                enabled = enabled.value,
                                actions = Actions.getActions(),
                                onRespond = onRespond
                            )
                        }
                        navController.navigate(Triggers.route)
                    } else {
                        coroutine.launch(Dispatchers.IO) {
                            ApiConnector.createRoutine(
                                token = token,
                                name = routineName.value,
                                description = routineDescription.value,
                                schedule = cronString,
                                enabled = enabled.value,
                                repeatable = repeatable.value,
                                actions = Actions.getActions(),
                                onRespond = onRespond
                            )
                        }
                        navController.navigate(Routines.route)
                    }
                }
            )
        },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.End
    )
}

@Composable
private fun InputText(
    modifier: Modifier,
    text: MutableState<String>,
    label: String,
    imeAction: ImeAction,
    keyboardType: KeyboardType,
    keyboardActions: KeyboardActions
) {
    val context = LocalContext.current

    TextField(
        modifier = modifier,
        value = text.value,
        onValueChange = { text.value = it },
        label = { Text(text = label) },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = imeAction
        ),
        keyboardActions = keyboardActions
    )
}

@Composable
private fun SwitchCard(
    title: String,
    check: MutableState<Boolean>
) {
    val selectedColor = if (check.value) LightSteelBlue else FadedLightGrey
    val notSelectedColor = if (!check.value) LightSteelBlue else FadedLightGrey

    Spacer(modifier = Modifier.height(20.dp))
    Column(
        modifier = Modifier
            .border(
                width = 4.dp,
                color = LightSteelBlue,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(10.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = title,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Row(
            modifier = Modifier.padding(top = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp)
                    .clickable(onClick = { check.value = false }),
                backgroundColor = notSelectedColor
            ) {
                Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (title == "Condition") "Lesser" else "Off",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp)
                    .clickable(onClick = { check.value = true }),
                backgroundColor = selectedColor
            ) {
                Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (title == "Condition") "Greater" else "On",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

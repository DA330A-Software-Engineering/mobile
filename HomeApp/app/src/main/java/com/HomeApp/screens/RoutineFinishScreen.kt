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
import com.HomeApp.ui.composables.RoutinesTitleBar
import com.HomeApp.ui.composables.RoutinesTitleBarItem
import com.HomeApp.ui.navigation.Routines
import com.HomeApp.ui.theme.FadedLightGrey
import com.HomeApp.ui.theme.LightSteelBlue
import com.HomeApp.util.ApiConnector
import com.HomeApp.util.ApiResult
import com.HomeApp.util.LocalStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Arrays

@Composable
fun RoutineFinishScreen(
    navController: NavController,
    OnSelfClick: () -> Unit = {}
) {
    val coroutine = rememberCoroutineScope()
    val context = LocalContext.current
    val token = LocalStorage.getToken(context)
    val listHeight = LocalConfiguration.current.screenHeightDp
    val cronString = Schedule.getCronString()

    val routineName = remember { mutableStateOf("") }
    val routineDescription = remember { mutableStateOf("") }

    val enabled = remember { mutableStateOf(true) }
    val repeatable = remember { mutableStateOf(true) }

    val onRespond: (ApiResult) -> Unit = {
        Log.d("RESPOND", it.toString())
    }

    val focusRequester = FocusRequester()
    val focusManager: FocusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            RoutinesTitleBar(
                item = RoutinesTitleBarItem.FinishRoutine,
                navController = navController
            )
        },
        content = {
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
                        InputText(
                            label = "Name",
                            text = routineName,
                            focusRequester = null,
                            imeAction = ImeAction.Next,
                            keyboardActions = KeyboardActions(onNext = { focusRequester.requestFocus() })
                        )
                    }
                    item {
                        InputText(
                            label = "Description",
                            text = routineDescription,
                            focusRequester = focusRequester,
                            imeAction = ImeAction.Done,
                            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                        )
                    }
                    item { SwitchCards(title = "Enabled", check = enabled) }
                    item { SwitchCards(title = "Repeatable", check = repeatable) }
                }
            )
        },
        floatingActionButton = {
            RoutinesFAB(
                icon = Icons.Rounded.Done,
                onClick = {
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
            )
        },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.End
    )
}

@Composable
private fun InputText(
    text: MutableState<String>,
    label: String,
    focusRequester: FocusRequester? = null,
    imeAction: ImeAction,
    keyboardActions: KeyboardActions
) {
    Spacer(modifier = Modifier.height(15.dp))
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .focusRequester(focusRequester ?: FocusRequester()),
        value = text.value,
        onValueChange = { text.value = it },
        label = { Text(text = label) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = imeAction
        ),
        keyboardActions = keyboardActions
    )
}

@Composable
private fun SwitchCards(
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
                        text = "Off",
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
                        text = "On",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

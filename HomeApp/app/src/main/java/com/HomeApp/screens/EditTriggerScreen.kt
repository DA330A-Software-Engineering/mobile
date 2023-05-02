package com.HomeApp.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.HomeApp.ui.composables.CustomFAB
import com.HomeApp.ui.composables.DeleteDialog
import com.HomeApp.ui.composables.SwitchCard
import com.HomeApp.ui.composables.TopTitleBar
import com.HomeApp.ui.composables.TopTitleBarItem
import com.HomeApp.ui.navigation.Triggers
import com.HomeApp.ui.theme.FadedLightGrey
import com.HomeApp.util.ApiConnector
import com.HomeApp.util.ApiResult
import com.HomeApp.util.LocalStorage
import com.HomeApp.util.getTrigger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun EditTriggerScreen(
    navController: NavController,
    OnSelfClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val token = LocalStorage.getToken(context)
    val coroutine = rememberCoroutineScope()
    val listHeight = LocalConfiguration.current.screenHeightDp
    val focusManager: FocusManager = LocalFocusManager.current
    val descriptionFocusRequester = FocusRequester()
    val resetValueFocusRequester = FocusRequester()

    val id = remember { mutableStateOf("") }
    val name = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val enabled = remember { mutableStateOf(true) }
    val condition = remember { mutableStateOf(true) }
    val value = remember { mutableStateOf("0") }
    val resetValue = remember { mutableStateOf("0") }

    LaunchedEffect(id, name, description, enabled, condition, value, resetValue) {
        getTrigger(context, SelectedItems.getTriggerId()) { document ->
            Log.d("document", document.toString())
            if (document != null) {
                id.value = document.id
                name.value = document.get("name") as String
                description.value = document.get("description") as String
                enabled.value = document.get("enabled") as Boolean
                condition.value = document.get("condition") as String == "grt"
                value.value = (document.get("value") as? Number?)?.toString() ?: "0"
                resetValue.value = (document.get("resetValue") as? Number?)?.toString() ?: "0"
            }
        }
    }

    val onRespond: (ApiResult) -> Unit = {
        Log.d("RESPOND", it.toString())
    }

    val deleteDialog = remember { mutableStateOf(false) }
    if (deleteDialog.value) {
        DeleteDialog(
            deleteDialog = deleteDialog,
            name = name.value,
            token = token,
            id = id.value
        )
    }

    Scaffold(
        topBar = {
            TopTitleBar(
                item = TopTitleBarItem.EditTrigger,
                navController = navController
            )
        },
        content = {
            LazyColumn(
                modifier = Modifier
                    .height(listHeight.dp)
                    .padding(it)
                    .padding(vertical = 10.dp)
                    .padding(top = 10.dp)
                    .padding(horizontal = 20.dp),
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
                            text = name,
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
                            text = description,
                            label = "Description",
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Text,
                            keyboardActions = KeyboardActions(
                                onDone = { focusManager.clearFocus() }
                            )
                        )
                    }
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
                    item { SwitchCard(title = "Enabled", check = enabled) }
                }
            )
        },
        floatingActionButton = {
            CustomFAB(
                icon = Icons.Rounded.Done,
                onClick = {
                    if (name.value.isEmpty() || name.value.isBlank()) {
                        val message = "Please enter a valid name"
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                        return@CustomFAB
                    }
                    if (description.value.isEmpty() || description.value.isBlank()) {
                        val message = "Please enter a valid description"
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                        return@CustomFAB
                    }
                    coroutine.launch(Dispatchers.IO) {
                        ApiConnector.updateTrigger(
                            token = token,
                            triggerId = id.value,
                            deviceId = SelectedItems.getSensorId(),
                            name = name.value,
                            description = description.value,
                            condition = if (condition.value) "grt" else "lsr",
                            value = value.value.toInt(),
                            resetValue = resetValue.value.toInt(),
                            enabled = enabled.value,
                            actions = Actions.getActions(),
                            onRespond = onRespond
                        )
                    }
                    navController.navigate(Triggers.route)
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
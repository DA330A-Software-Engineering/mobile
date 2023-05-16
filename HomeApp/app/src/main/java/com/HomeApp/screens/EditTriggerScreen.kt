package com.HomeApp.screens

import android.util.Log
import android.widget.Toast
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
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
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
import com.HomeApp.ui.composables.TitleBar
import com.HomeApp.ui.navigation.ChooseType
import com.HomeApp.ui.navigation.Triggers
import com.HomeApp.ui.theme.DarkRed
import com.HomeApp.ui.theme.FadedLightGrey
import com.HomeApp.ui.theme.LightSteelBlue
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

    val id = remember { mutableStateOf("") }
    val name = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val enabled = remember { mutableStateOf(true) }
    val condition = remember { mutableStateOf(true) }
    val value = remember { mutableStateOf("0") }
    val resetValue = remember { mutableStateOf("0") }

    LaunchedEffect(id, name, description, enabled, condition, value, resetValue) {
        getTrigger(context, SelectedItems.getTriggerId()) { document ->
            if (document != null) {
                id.value = document.id
                name.value = document.get("name") as String
                description.value = document.get("description") as String
                enabled.value = document.get("enabled") as Boolean
                condition.value = document.get("condition") as String == "grt"
                value.value = (document.get("value") as? Number?)?.toString().toString()
                resetValue.value = (document.get("resetValue") as? Number?)?.toString().toString()
            }
        }
    }

    val onRespond: (ApiResult) -> Unit = {
        Log.d("RESPOND", it.toString())
    }

    Scaffold(
        topBar = {
            TitleBar(
                title = "Edit",
                iconLeft = Icons.Rounded.ArrowBack,
                routeLeftButton = Triggers.route,
                iconRight = null,
                routeRightButton = null,
                navController = navController
            )
        },
        content = { it ->
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
                    item { Spacer(modifier = Modifier.height(15.dp)) }
                    item {
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            value = name.value,
                            onValueChange = { name.value = it },
                            label = { Text(text = "Name") },
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Sentences,
                                autoCorrect = false,
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.clearFocus() }
                            )
                        )
                    }
                    item { Spacer(modifier = Modifier.height(15.dp)) }
                    item {
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            value = description.value,
                            onValueChange = { description.value = it },
                            label = { Text(text = "Description") },
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Sentences,
                                autoCorrect = true,
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done
                            ),
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
                    }
                    item {
                        Text(
                            text = "Choose when the actions should execute",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                    item { Spacer(modifier = Modifier.height(15.dp)) }
                    item {
                        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                            TextField(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 30.dp),
                                value = value.value,
                                onValueChange = { value.value = it },
                                label = { Text(text = "Value") },
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.None,
                                    autoCorrect = false,
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Next
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = {
                                        val newValue = value.value.toIntOrNull()
                                        if (newValue == null) {
                                            value.value = "0"
                                        } else if (newValue >= 1023) {
                                            value.value = "1023"
                                        }
                                        focusManager.clearFocus()
                                    }
                                )
                            )
                            TextField(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 30.dp),
                                value = resetValue.value,
                                onValueChange = { resetValue.value = it },
                                label = { Text(text = "Reset value") },
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.None,
                                    autoCorrect = false,
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        val newValue = resetValue.value.toIntOrNull()
                                        if (newValue == null) {
                                            resetValue.value = "0"
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
                    item {
                        ManageCards(
                            name = name.value,
                            token = token,
                            id = id.value,
                            navController = navController
                        )
                    }
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
private fun ManageCards(
    name: String,
    token: String,
    id: String,
    navController: NavController
) {
    val deleteDialog = remember { mutableStateOf(false) }
    if (deleteDialog.value) {
        DeleteDialog(
            deleteDialog = deleteDialog,
            name = name,
            token = token,
            id = id,
            navController = navController
        )
    }

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
            text = "Manage",
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
                    .clickable(onClick = {
                        SelectedItems.setIsEdit(true)
                        navController.navigate(ChooseType.route)
                    }),
                backgroundColor = LightSteelBlue
            ) {
                Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Actions",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp)
                    .clickable(onClick = { deleteDialog.value = true }),
                backgroundColor = DarkRed
            ) {
                Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Delete",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

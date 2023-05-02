package com.HomeApp.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.HomeApp.ui.composables.DeleteDialog
import com.HomeApp.ui.composables.TopTitleBar
import com.HomeApp.ui.composables.TopTitleBarItem
import com.HomeApp.ui.theme.DarkRed
import com.HomeApp.ui.theme.FadedLightGrey
import com.HomeApp.ui.theme.GhostWhite
import com.HomeApp.ui.theme.LightSteelBlue
import com.HomeApp.util.ApiConnector
import com.HomeApp.util.ApiResult
import com.HomeApp.util.LocalStorage
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun TriggersScreen(
    navController: NavController,
    OnSelfClick: () -> Unit = {}
) {
    SelectedItems.setIsSensor(true)

    val listHeight = LocalConfiguration.current.screenHeightDp
    val documents = realTimeData!!.triggers

    Scaffold(
        topBar = {
            TopTitleBar(
                item = TopTitleBarItem.Triggers,
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
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(items = documents, key = { item -> item.id }) { item ->
                    TriggerCard(triggerItem = item)
                }
            }
        }
    )
}

@Composable
private fun TriggerCard(triggerItem: DocumentSnapshot) {
    val context = LocalContext.current
    val token = LocalStorage.getToken(context)
    val coroutine = rememberCoroutineScope()
    val focusManager: FocusManager = LocalFocusManager.current

    val id = remember { mutableStateOf(triggerItem.id) }
    val name = remember { mutableStateOf(triggerItem.get("name") as String) }
    val description = remember { mutableStateOf(triggerItem.get("description") as String) }
    val enabled = remember { mutableStateOf(triggerItem.get("enabled") as Boolean) }
    val conditionValue = remember { mutableStateOf(triggerItem.get("condition") as String) }
    val value = remember { mutableStateOf(triggerItem.get("value") as Number) }
    val resetValue = remember { mutableStateOf(triggerItem.get("resetValue") as Number) }

    val onRespond: (ApiResult) -> Unit = {
        Log.d("RESPOND", it.toString())
    }

    val deleteDialog = remember { mutableStateOf(false) }
    if (deleteDialog.value) {
        DeleteDialog(
            deleteDialog = deleteDialog,
            name = name.value,
            token = token,
            id = id.value,
            onRespond = onRespond
        )
    }

    fun updateTrigger() {
        coroutine.launch(Dispatchers.IO) {
            ApiConnector.updateTrigger(
                token = token,
                triggerId = id.value,
                deviceId = SelectedItems.getSensorId(),
                name = name.value,
                description = description.value,
                condition = conditionValue.value,
                value = value.value,
                resetValue = resetValue.value,
                enabled = enabled.value,
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
        Card(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = {
                    enabled.value = !enabled.value
                    updateTrigger()
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
                                        updateTrigger()
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
                                        updateTrigger()
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
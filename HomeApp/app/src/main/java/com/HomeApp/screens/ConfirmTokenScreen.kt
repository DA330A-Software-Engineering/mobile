package com.HomeApp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.HomeApp.ui.composables.BottomDivider
import com.HomeApp.ui.composables.Divider.SignIn
import com.HomeApp.ui.composables.InputType
import com.HomeApp.ui.composables.TextInput
import com.HomeApp.ui.navigation.ResetPassword

@Composable
fun ConfirmTokenScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    OnSelfClick: () -> Unit = {}
) {
    val focusManager: FocusManager = LocalFocusManager.current
    var emailToken by remember { mutableStateOf("") }
    Column(
        Modifier
            .padding(24.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.Bottom)
    ) {
        TextInput(
            inputType = InputType.Token,
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            updateValue = { emailToken = it }
        )
        Button(
            onClick = { navController.navigate(ResetPassword.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("SUBMIT", Modifier.padding(vertical = 8.dp))
        }
        BottomDivider(divider = SignIn, navController = navController)
    }
}

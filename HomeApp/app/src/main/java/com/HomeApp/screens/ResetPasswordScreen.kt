package com.HomeApp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.HomeApp.ui.composables.InputType
import com.HomeApp.ui.composables.TextInput
import androidx.navigation.NavController
import com.HomeApp.ui.composables.BottomDivider
import com.HomeApp.ui.composables.Divider.SignIn
import com.HomeApp.ui.navigation.Login

@Composable
fun ResetPasswordScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    OnSelfClick: () -> Unit = {}
) {
    val passwordFocusRequester = FocusRequester()
    val focusManager: FocusManager = LocalFocusManager.current

    Column(
        Modifier
            .padding(24.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.Bottom)
    ) {
        TextInput(
            inputType = InputType.NewPassword,
            keyboardActions = KeyboardActions(onNext = { passwordFocusRequester.requestFocus() })
        )
        TextInput(
            inputType = InputType.ConfirmPassword,
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            focusRequester = passwordFocusRequester
        )
        Button(onClick = { navController.navigate(Login.route) }, modifier = Modifier.fillMaxWidth()) {
            Text("RESET PASSWORD", Modifier.padding(vertical = 8.dp))
        }
        BottomDivider(divider = SignIn, navController = navController)
    }
}

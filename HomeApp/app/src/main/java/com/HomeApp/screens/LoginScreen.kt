package com.HomeApp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.HomeApp.ui.composables.BottomDivider
import com.HomeApp.ui.composables.Divider
import com.HomeApp.ui.composables.InputType
import com.HomeApp.ui.composables.TextInput

@Composable
fun LoginScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    OnSelfClick: () -> Unit = {}
) {
    val passwordFocusRequester = FocusRequester()
    val focusManager: FocusManager = LocalFocusManager.current

    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            16.dp, alignment = Alignment.Bottom
        )
    ) {
        TextInput(
            InputType.Email,
            keyboardActions = KeyboardActions(onNext = { passwordFocusRequester.requestFocus() })
        )
        TextInput(
            InputType.Password,
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            focusRequester = passwordFocusRequester
        )
        Button(
            onClick = { navController.navigate("Home") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("SIGN IN", Modifier.padding(vertical = 8.dp))
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = { navController.navigate("forgot-password") }) {
                Text("Forgot your password?")
            }
        }
        BottomDivider(divider = Divider.SignUp, navController = navController)
    }
}

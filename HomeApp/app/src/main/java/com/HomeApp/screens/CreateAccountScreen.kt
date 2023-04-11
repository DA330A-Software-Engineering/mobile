package com.HomeApp.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.HomeApp.ui.composables.BottomDivider
import com.HomeApp.ui.composables.Divider.SignIn
import com.HomeApp.ui.composables.InputType
import com.HomeApp.ui.composables.TextInput
import com.HomeApp.ui.navigation.Loading
import com.HomeApp.util.ApiConnector
import com.HomeApp.util.ApiResult
import com.HomeApp.util.HttpStatus
import com.HomeApp.util.LocalStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject


@Composable
fun CreateAccountScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    OnSelfClick: () -> Unit = {}
) {
    val newPasswordFocusRequester = FocusRequester()
    val confirmPasswordFocusRequester = FocusRequester()
    val focusManager: FocusManager = LocalFocusManager.current
    val coroutine = rememberCoroutineScope()
    val context = LocalContext.current

    val onRespond: (ApiResult) -> Unit = {
        val TAG = "RESPOND"
//        Log.d(TAG, it.toString())
        val data: JSONObject = it.data()
//    val msg: String = data.get("msg") as String
        when (it.status()) {
            HttpStatus.SUCCESS -> {
                Log.d(TAG, "${data.get("token")}")
                LocalStorage.saveToken(context, data.get("token").toString())
                coroutine.launch(Dispatchers.Main) {
                    navController.navigate(Loading.route)
                }
            }
            HttpStatus.UNAUTHORIZED -> {
                Log.d(TAG, it.data().toString())
            }
            HttpStatus.FAILED -> {

            }
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            16.dp, alignment = Alignment.Bottom
        )
    ) {
        var name by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var pw1 by remember { mutableStateOf("") }
        var pw2 by remember { mutableStateOf("") }

        TextInput(
            InputType.Name,
            keyboardActions = KeyboardActions(onNext = { newPasswordFocusRequester.requestFocus() }),
            updateValue = { name = it }
        )
        TextInput(
            InputType.Email,
            keyboardActions = KeyboardActions(onNext = { newPasswordFocusRequester.requestFocus() }),
            updateValue = { email = it }
        )
        TextInput(
            inputType = InputType.NewPassword,
            keyboardActions = KeyboardActions(onNext = { confirmPasswordFocusRequester.requestFocus() }),
            focusRequester = newPasswordFocusRequester,
            updateValue = { pw1 = it }
        )
        TextInput(
            InputType.ConfirmPassword,
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            focusRequester = confirmPasswordFocusRequester,
            updateValue = { pw2 = it }
        )
        Button(
            onClick = {
                coroutine.launch(Dispatchers.IO) {
                    ApiConnector.createAccount(
                        name = name,
                        email = email,
                        password = pw1,
                        onRespond = onRespond
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("SIGN UP", Modifier.padding(vertical = 8.dp))
        }
        BottomDivider(divider = SignIn, navController = navController)
    }
}

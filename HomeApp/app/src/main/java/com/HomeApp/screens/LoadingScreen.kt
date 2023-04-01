package com.HomeApp.screens

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.HomeApp.ui.navigation.Home
import com.HomeApp.ui.navigation.Login
import com.HomeApp.ui.navigation.navigateSingleTopTo
import com.HomeApp.util.ApiConnector
import com.HomeApp.util.ApiResult
import com.HomeApp.util.HttpStatus
import com.HomeApp.util.LocalStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LoadingScreen(
    navController: NavController
){
    // screen for loading while checking token
    // Backend respond, if we have an valid token
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    var isAuth by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(true) }
    val coroutine = rememberCoroutineScope()
    val context = LocalContext.current

    val onAuth: (ApiResult) -> Unit = {
        isAuth = it.status() == HttpStatus.SUCCESS
        loading = false
        if (!isAuth) navController.navigateSingleTopTo(Login.route)
        else navController.navigateSingleTopTo(Home.route)
    }

    LaunchedEffect(navBackStackEntry) {
        launch {
            // Call backend to check if we already have an valid token
            coroutine.launch(Dispatchers.IO) {
                ApiConnector.getUserData(
                    token = LocalStorage.getToken(context),
                    onRespond = { onAuth(it) }
                )
            }
        }
    }


}

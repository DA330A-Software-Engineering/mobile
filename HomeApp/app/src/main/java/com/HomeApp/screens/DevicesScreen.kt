package com.HomeApp.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.HomeApp.ui.composables.TitleBar
import com.HomeApp.ui.navigation.NavPath

@Composable
fun DevicesScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    OnSelfClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TitleBar(screenTitle = "Devices", previousScreen = navController.currentBackStackEntry?.destination?.route?)
        },
        content = {
            Column(modifier = Modifier.padding(it)) {
                
            }
        },
        bottomBar = {

        }
    )
}

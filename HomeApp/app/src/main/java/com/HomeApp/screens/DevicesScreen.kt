package com.HomeApp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
            TitleBar(screenTitle = "Devices", navController = navController)
        },
        content = {
            Column(modifier = Modifier.padding(it)) {

            }
        },
        bottomBar = {

        }
    )
}

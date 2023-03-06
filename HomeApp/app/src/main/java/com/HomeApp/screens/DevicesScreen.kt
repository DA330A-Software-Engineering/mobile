package com.HomeApp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.HomeApp.ui.composables.FilteredList
import com.HomeApp.ui.composables.TitleBar
import com.HomeApp.ui.composables.TitledDivider
import com.HomeApp.ui.navigation.NavPath
import com.HomeApp.util.DevicesFilters


@Composable
fun DevicesScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    state: ScaffoldState,
    OnSelfClick: () -> Unit = {}
) {
    var filtersSelected by remember { mutableStateOf("") }

    Scaffold(
        topBar = {

            TitleBar(screenTitle = "Devices", navController = navController)
        },
        content = {
            Column(modifier = Modifier.padding(it)) {
                TitledDivider(navController = navController, title = "Filters")
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)) {
                    DevicesFilters.values().forEach {
                        Button(onClick = { /*TODO*/ }) {

                        }
                    }
                }

            }
        },
        bottomBar = {

        }
    )
}

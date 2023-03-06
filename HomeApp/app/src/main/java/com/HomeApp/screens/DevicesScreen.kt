package com.HomeApp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.HomeApp.R
import com.HomeApp.ui.composables.AppFooter

@Composable
fun DevicesScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    OnSelfClick: () -> Unit = {},
    state: ScaffoldState
) {
    val bgCol = if (MaterialTheme.colors.isLight) colorResource(
        id = R.color.GhostWhite
    ) else Color(0xFF313338)
    val spacerHeight: Dp = 112.dp
    Column(
        Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.GhostWhite))
    ) {
        Scaffold(
            content = {
                Column(
                    Modifier
                        .fillMaxSize()
                        .background(bgCol)
                        .padding(it)
                        .padding(horizontal = 5.dp),
                ) {
                    /* !TODO -- CONTENT here */
                }
            },
            bottomBar = {
                AppFooter(
                    navController = navController,
                    bgCol = bgCol,
                    micColor = if (MaterialTheme.colors.isLight) Color.Black else Color.White
                )
            })

    }
}

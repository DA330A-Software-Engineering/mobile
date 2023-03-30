package com.HomeApp.screens

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.HomeApp.ui.navigation.Home

@Composable
fun ProfileScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    OnSelfClick: () -> Unit = {}
) {
    // Temporary button, remove if needed
    Button(onClick = { navController.navigate(Home.route) }) {
        Text(text = "Go Back")
    }
}

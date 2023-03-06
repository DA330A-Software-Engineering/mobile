package com.HomeApp.ui.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.Navigation

@Composable

fun TitleBar(
    screenTitle: String,
    previousScreen: String, // This is to know what screen to navigate to when pressing back

    modifier: Modifier = Modifier,
    navController: NavController

) {
    Row(modifier = Modifier.height(30.dp)) {
        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "arrow-back")
        Spacer(modifier = Modifier.weight(1f))
        Text(text = screenTitle, modifier = Modifier.weight(2f))
        Spacer(modifier = Modifier.weight(1f))


    }

}
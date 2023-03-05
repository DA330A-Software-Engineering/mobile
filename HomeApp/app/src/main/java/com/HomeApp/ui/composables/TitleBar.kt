package com.HomeApp.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.Navigation

@Composable

fun TitleBar(
    screenTitle: String,
    previousScreen: String, // This is to know what screen to navigate to when pressing back

    modifier: Modifier = Modifier,
    navigation: Navigation

) {
    Row(modifier = Modifier.height(30.dp)) {


    }

}
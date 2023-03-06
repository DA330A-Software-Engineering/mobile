package com.HomeApp.ui.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.Navigation


@Composable
fun TitleBar(
    screenTitle: String,
    //previousScreen: String, This is to know what screen to navigate to when pressing back

    modifier: Modifier = Modifier,
    navController: NavController

) {
    Row(
        modifier = Modifier
            .height(60.dp)
            .padding(horizontal = 20.dp)) {
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "arrow-back",
                modifier = Modifier.size(50.dp)
                    .padding(top = 10.dp))
        }
        Text(text = screenTitle,
            modifier = Modifier.weight(2f),
            fontSize = 40.sp,
            textAlign = TextAlign.Center)
        IconButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Outlined.Settings,
                contentDescription = "settings-icon",
                modifier = Modifier
                    .size(50.dp)
                    .padding(top = 10.dp))
        }
    }

}
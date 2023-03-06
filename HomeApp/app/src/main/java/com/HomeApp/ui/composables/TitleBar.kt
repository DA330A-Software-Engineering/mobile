package com.HomeApp.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlin.math.round


@Composable
fun TitleBar(
    screenTitle: String,
    //previousScreen: String, This is to know what screen to navigate to when pressing back

    modifier: Modifier = Modifier,
    navController: NavController

) {
    val selectedColor = 0xFFAECCE4
    val notSelectedColor = 0xFFC5C5C5

    var leftSelected by remember { mutableStateOf(true) }

    Column(modifier = Modifier) {
        Spacer(modifier = Modifier.height(30.dp))
        Row(
            modifier = Modifier
                .height(60.dp)
                .padding(horizontal = 20.dp)) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "arrow-back",
                    modifier = Modifier
                        .size(50.dp)
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
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(25.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)) {
            Button(onClick = { leftSelected = true }, modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
                colors = ButtonDefaults.buttonColors(backgroundColor = if (leftSelected) Color(selectedColor) else Color(notSelectedColor)
                ),
                shape = RoundedCornerShape(0.dp)
            ) {
                Column(modifier = Modifier
                    .fillMaxSize()) {
                    Text(text = "All", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                    Text(text = "12", textAlign = TextAlign.Center, color = Color.DarkGray, modifier = Modifier.fillMaxWidth())
                }
            }
            Button(onClick = { leftSelected = false }, modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
                colors = ButtonDefaults.buttonColors(if (leftSelected) Color(notSelectedColor) else Color(selectedColor)),
                shape = RoundedCornerShape(0.dp)
            ) {
                Column(modifier = Modifier
                    .fillMaxSize()) {
                    Text(text = "Favourites", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                    Text(text = "3", textAlign = TextAlign.Center, color = Color.DarkGray, modifier = Modifier.fillMaxWidth())
                }
            }
        }
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(10.dp))
    }
}

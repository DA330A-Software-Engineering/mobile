package com.HomeApp.ui.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun TitleBar(
    modifier: Modifier = Modifier,
    screenTitle: String,
    isDevices: Boolean = false,
    navController: NavController
) {
    Column(
        modifier = Modifier.then(modifier), verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Row(
            modifier = Modifier
                .height(60.dp)
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!isDevices) {
                Box(modifier = Modifier.width(50.dp)) {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "arrow-back",
                            modifier = Modifier
                                .size(35.dp)
                        )
                    }
                }
            }
            Text(
                text = screenTitle,
                modifier = Modifier.weight(2f),
                fontSize = 30.sp,
                textAlign = TextAlign.Center
            )
//            IconButton(onClick = { /*TODO*/ }) {
//                Icon(
//                    imageVector = Icons.Outlined.Settings,
//                    contentDescription = "settings-icon",
//                    modifier = Modifier
//                        .size(25.dp)
//                )
//            }
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(25.dp)
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
        )
    }
}

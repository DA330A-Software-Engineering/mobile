package com.HomeApp.ui.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun TitledDivider(
    navController: NavController,
    title: String,
    description: String = "Titled Divider",
    modifier: Modifier = Modifier,
    showIcon: Boolean = false
) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(35.dp)
            .padding(horizontal = 10.dp)
            .padding(5.dp)
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title)
        Divider(
            Modifier
                .weight(14f) // needs to be dynamic
                .alpha(1f)
                .padding(top = 3.dp)
                .padding(horizontal = 15.dp),
            color = Color.Gray,
            thickness = 1.dp
        )
        if (showIcon) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Rounded.Edit, contentDescription = description)
            }
        }
    }
}

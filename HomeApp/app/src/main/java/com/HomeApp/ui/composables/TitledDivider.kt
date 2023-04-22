package com.HomeApp.ui.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun TitledDivider(
    navController: NavController,
    title: String,
    description: String = "Titled Divider",
    modifier: Modifier = Modifier,
    showIcon: Boolean = false,
    fontSize: Int = 20
) {
    var addGroupState by remember { mutableStateOf(false) }
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .padding(5.dp)
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontSize = fontSize.sp)
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
            IconButton(onClick = { addGroupState = true }, modifier = Modifier.size(50.dp)) {
                if (title == "Groups") {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = description,
                        modifier = Modifier.scale(1.5f)
                    )
                } else {
                    Icon(imageVector = Icons.Rounded.Edit, contentDescription = description)
                }

            }
        }


        if (addGroupState && title == "Groups") {
            AlertDialog(
                onDismissRequest = { addGroupState = false },
                title = { Text(text = "Add new group") },
                text = { AddGroup(onCreate = {newState -> addGroupState = newState}) },
                confirmButton = {
                    Button(
                        onClick = { addGroupState = false },
                    ) {
                        Text("Close")
                    }
                }
            )
        }
    }
}

package com.HomeApp.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.Navigation
import com.HomeApp.ui.navigation.Settings
import com.HomeApp.ui.theme.RaminGrey
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.launch


data class GroupsClass(
    var id: String = "",
    var name: String = "",
    var description: String = "",
    //var state: String = "",
    var devices: List<String> = emptyList()
)


@Composable
fun GroupComposable(
    modifier: Modifier = Modifier,
    groupItem: DocumentSnapshot,
    //groupState: String
) {
    var editGroup by remember { mutableStateOf(false) }
    Button(onClick = { /*TODO*/ }, modifier = Modifier
        .border(
            width = 1.dp,
            shape = RoundedCornerShape(10.dp),
            color = RaminGrey
        )
        .width(110.dp)
        .height(100.dp),
        contentPadding = PaddingValues(start = 0.dp, top = 8.dp, end = 0.dp, bottom = 0.dp),
        shape = RoundedCornerShape(10.dp)) {
        Column(modifier = Modifier.padding(horizontal = 15.dp)) {
            Text(text = groupItem.get("name") as String, modifier = Modifier.fillMaxWidth(), textDecoration = TextDecoration.Underline, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(15.dp))
            Row(modifier = Modifier) {
                Text(text = "On", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = {
                              editGroup = true

                    }, modifier = Modifier
                        .size(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Settings, contentDescription = "Settings Button",
                        modifier = Modifier.scale(1f),
                        tint = Color.White
                    )
                }
            }

        }

        if (editGroup) {
            AlertDialog(
                onDismissRequest = { editGroup = false },
                title = { Text(groupItem.get("name") as String) },
                text = { editGroup()},
                confirmButton = {
                    Button(
                        onClick = { editGroup = false },
                    ) {
                        Text("Done")
                    }
                }
            )
        }

    }
}

@Composable
private fun editGroup(
    modifier: Modifier= Modifier
) {
    Column(modifier = Modifier) {

    }
}
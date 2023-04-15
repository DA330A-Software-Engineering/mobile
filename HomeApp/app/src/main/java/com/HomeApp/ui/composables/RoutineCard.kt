package com.HomeApp.ui.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.DoorFront
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.HomeApp.ui.theme.RaminGrey
import com.google.firebase.firestore.DocumentSnapshot

@Composable
fun RoutineCard(
    navController: NavController,
    modifier: Modifier = Modifier,
    RoutineItem: DocumentSnapshot
) {
    val coroutine = rememberCoroutineScope()

    Row(modifier = Modifier.height(60.dp)) {
        Button(
            onClick = {
            },
            modifier = modifier.then(
                Modifier
                    .border(
                        width = 1.dp,
                        shape = RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp),
                        color = RaminGrey
                    )
                    .fillMaxHeight()
                    .weight(1f)
            ),
            colors = ButtonDefaults.buttonColors(backgroundColor = RaminGrey),
            elevation = null
        ) {
        }
        Button(
            onClick = {
            },
            modifier = modifier.then(
                Modifier
                    .border(
                        width = 1.dp,
                        shape = RoundedCornerShape(topEnd = 5.dp, bottomEnd = 5.dp),
                        color = RaminGrey
                    )
                    .fillMaxHeight()
                    .weight(5f)
            ),
            elevation = null,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 3.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.BrokenImage,
                        contentDescription = "",
                        modifier = Modifier
                            .size(48.dp)
                    )
                    Spacer(modifier = Modifier.width(7.dp))
                    Text(
                        text = "",
                        fontSize = 25.sp,
                        modifier = Modifier
                            .fillMaxHeight()
                            .wrapContentHeight(align = Alignment.CenterVertically)
                    )
                }
                Text(
                    text = "",
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .wrapContentHeight(align = Alignment.CenterVertically)
                        .padding(end = 7.dp),
                    textAlign = TextAlign.Right,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}
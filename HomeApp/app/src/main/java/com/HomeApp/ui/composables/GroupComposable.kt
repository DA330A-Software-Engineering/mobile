package com.HomeApp.ui.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.Navigation
import com.HomeApp.ui.theme.RaminGrey

data class Groups(
    var id: String = "",
    var name: String = "",
    var description: String = "",
    var available: List<String> = emptyList()
)

@Composable
fun GroupComposable(
    modifier: Modifier = Modifier,
    groupName: String,
    groupState: String
) {

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
        Column(modifier = Modifier) {
            Text(text = groupName, modifier = Modifier.fillMaxWidth(),textAlign = TextAlign.Center, textDecoration = TextDecoration.Underline, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(15.dp))
            Text(text = groupState, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        }

    }
}
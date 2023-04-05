package com.HomeApp.ui.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.Navigation

data class Groups(
    var id: String = "",
    var name: String = "",
    var description: String = "",
    var state: Boolean = false,
    var available: Boolean = true
)

@Composable
fun GroupComposable(
    modifier: Modifier = Modifier,
    groupName: String,
    groupState: String
) {
    Column(modifier = Modifier
        .width(20.dp)
        .height(20.dp)
    ) {
        Text(text = groupName, modifier = Modifier.padding(horizontal = 3.dp))
        Spacer(modifier = Modifier.height(5.dp))
        Text(text = groupState, modifier = Modifier.padding(horizontal = 3.dp))
    }
}
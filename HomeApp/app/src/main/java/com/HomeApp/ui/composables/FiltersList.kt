package com.HomeApp.ui.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.HomeApp.util.DevicesFilters


@Composable
fun FilteredList(
    listOfFilters: Array<DevicesFilters> ,
    modifier: Modifier = Modifier
) {

    Row(modifier = Modifier) {
        listOfFilters.forEach {

        }
    }

}

@Composable
private fun FilterIcons() {

}
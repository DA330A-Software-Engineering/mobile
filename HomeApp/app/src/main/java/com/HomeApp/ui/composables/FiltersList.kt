package com.HomeApp.ui.composables

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.HomeApp.util.DevicesFilters


@Composable
fun FilteredList(
    modifier: Modifier = Modifier,
    filterScreen: String
) {

    val filtersSelected = remember { mutableStateListOf<String>() }
    var itemChanged by remember { mutableStateOf(true) }
    

    if(filterScreen == "devices") {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 55.dp)) {
            DevicesFilters.values().forEach {
                Button(onClick = {
                    itemChanged = !itemChanged
                    if (filtersSelected.contains(it.filterValue)){
                        filtersSelected.remove(it.filterValue)
                    }
                    else filtersSelected.add(it.filterValue)
                    Log.d(TAG, filtersSelected.toString())

                },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(backgroundColor = if (filtersSelected.contains(it.filterValue)) Color(0xFFAECCE4) else Color(0xFFC5C5C5)),
                    modifier = Modifier.size(70.dp)
                ) {
                    Icon(imageVector = it.filterIcon, contentDescription = it.filterValue, modifier = Modifier.size(100.dp))
                }
                if (it.filterName != "Doors"){
                    Spacer(modifier = Modifier.weight(0.5f))
                }

            }
        }
    }



}
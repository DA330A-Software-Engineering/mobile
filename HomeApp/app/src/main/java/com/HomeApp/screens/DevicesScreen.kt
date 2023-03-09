package com.HomeApp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.HomeApp.ui.composables.FilteredList
import com.HomeApp.ui.composables.TitleBar
import com.HomeApp.ui.composables.TitledDivider
import com.HomeApp.ui.navigation.NavPath
import com.HomeApp.util.DevicesFilters
import org.json.JSONObject
import java.util.Objects

sealed interface DEVICES{
    var id: String
    var type: String
    var state: JSONObject
    var name: String
}

class DevicesDummy: DEVICES{
    override var id: String = ""
    override var type: String= ""
    override var name: String = ""
    override var state: JSONObject = JSONObject()
}



@Composable
fun DevicesScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    state: ScaffoldState,
    OnSelfClick: () -> Unit = {}
) {
    //var filtersSelected by remember { mutab }

    val device1 = DevicesDummy()
    device1.id = "11"
    device1.type = "light"
    device1.name = "Living Room Light"
    device1.state = JSONObject("{'name': 'hi'}")
    val device2 = DevicesDummy()
    device1.id = "11"
    device1.type = "light"
    device1.name = "Living Room Light"
    device1.state = JSONObject("{'name': 'hi'}")
    val device3 = DevicesDummy()
    device1.id = "11"
    device1.type = "light"
    device1.name = "Living Room Light"
    device1.state = JSONObject("{'name': 'hi'}")

    val devicesList = remember {
        mutableListOf(device1, device2, device3)
    }


    Scaffold(
        topBar = {

            TitleBar(screenTitle = "Devices", navController = navController)
        },
        content = {
            Column(modifier = Modifier
                .padding(it)
                .height(200.dp)) {
                TitledDivider(navController = navController, title = "Filters")
                FilteredList(filterScreen="devices")

                /*LazyColumn(content = {
                    devicesList.forEach({
                        item {

                        }
                    })
                })*/

            }
        },
        bottomBar = {

        }
    )
}
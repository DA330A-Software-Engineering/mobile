package com.HomeApp.screens

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.HomeApp.ui.composables.*
import com.HomeApp.ui.theme.GhostWhite
import com.HomeApp.util.microphoneIcon
import org.json.JSONObject

sealed interface DEVICES {
    var id: String
    var type: String
    var state: JSONObject
    var name: String
}

class DevicesDummy : DEVICES {
    override var id: String = ""
    override var type: String = ""
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
    device1.state = JSONObject("{'on': 'true'}")
    val device2 = DevicesDummy()
    device2.id = "12"
    device2.type = "curtain"
    device2.name = "bed Room Light"
    device2.state = JSONObject("{'open': 'false'}")
    val device4 = DevicesDummy()
    device4.id = "13"
    device4.type = "door"
    device4.name = "Balcony Door"
    device4.state = JSONObject("{'open': 'true'}")
    val device5 = DevicesDummy()
    device5.id = "13"
    device5.type = "door"
    device5.name = "Front Door"
    device5.state = JSONObject("{'open': 'false'}")
    val device6 = DevicesDummy()
    device6.id = "13"
    device6.type = "light"
    device6.name = "Bedroom light 2"
    device6.state = JSONObject("{'on': 'false'}")
    val device8 = DevicesDummy()
    device8.id = "13"
    device8.type = "light"
    device8.name = "Balcony light"
    device8.state = JSONObject("{'on': 'true'}")
    val device7 = DevicesDummy()
    device7.id = "13"
    device7.type = "door"
    device7.name = "Back Door"
    device7.state = JSONObject("{'open': 'false'}")

    val devicesList = listOf(device1, device2, device4, device5, device6, device7, device8)
    val listHeight = LocalConfiguration.current.screenHeightDp


    Scaffold(
        topBar = {
            TitleBar(screenTitle = "Devices", navController = navController)
        },
        content = {
            Log.d(TAG, listHeight.toString())
            LazyColumn(
                modifier = Modifier
                    .padding(it)
                    .fillMaxHeight()
            ) {
                item { TitledDivider(navController = navController, title = "Filters") }
                item { FilteredList(filterScreen = "devices") }
                item {
                    LazyColumn(
                        modifier = Modifier
                            .height(listHeight.dp)
                            .padding(vertical = 10.dp)
                            .padding(horizontal = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(items = devicesList, key = { item -> item.name }) { item ->
                            DeviceCard(navController = navController, deviceItem = item)
                        }
                    }
                }
            }
        },
        bottomBar = {
            AppFooter(
                navController = navController,
                bgCol = MaterialTheme.colors.background,
                micColor = if (MaterialTheme.colors.isLight) Color.Black else Color.White
            )
        }, floatingActionButton = {
            FloatingActionButton(
                onClick = { /*TODO*/ },
                backgroundColor = GhostWhite,
                modifier = Modifier.scale(1f)
            ) {
                Icon(
                    tint = Color.Black,
                    imageVector = microphoneIcon,
                    contentDescription = "Mic",
                    modifier = Modifier.scale(1.4f)
                )
            }
        }, isFloatingActionButtonDocked = true, floatingActionButtonPosition = FabPosition.Center
    )
}

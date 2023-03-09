package com.HomeApp.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Curtains
import androidx.compose.material.icons.filled.DoorFront
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.HomeApp.R
import com.HomeApp.screens.DevicesDummy
import kotlin.math.round

@Composable
fun DeviceCard(
    navController: NavController,
    modifier: Modifier = Modifier,
    deviceItem: DevicesDummy
) {

    val cardIcon: ImageVector = when (deviceItem.type){
        "light" -> Icons.Filled.Lightbulb
        "door"-> Icons.Filled.DoorFront
        "curtain" -> Icons.Filled.Curtains
        else -> Icons.Filled.BrokenImage
    }

    val deviceState: String = when(deviceItem.type) {
        "light" -> if (deviceItem.state.getString("on") == "true") "On" else "Off"
        "door" -> if (deviceItem.state.getString("open") == "true") "Open" else "Closed"
        "curtain" -> if (deviceItem.state.getString("open") == "true") "Open" else "Open"
        else -> {"No State"}
    }

    Button(onClick = { /*TODO*/ },
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor =colorResource(id = R.color.LightSteelBlue)),
        shape = RoundedCornerShape(10)
    ) {
        Row(modifier = Modifier.fillMaxWidth()){
            Spacer(modifier = Modifier.width(3.dp))
            Icon(imageVector = cardIcon,
                contentDescription =deviceItem.type,
                modifier= Modifier.size(70.dp).padding(top=7.dp))
            Spacer(modifier = Modifier.width(7.dp))
            Text(text = deviceItem.name,
                fontSize = 25.sp,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(220.dp)
                    .wrapContentHeight(align = Alignment.CenterVertically),
                style = TextStyle(textDecoration = TextDecoration.Underline)
            )
            Text(text = deviceState,
                fontSize = 18.sp,
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .wrapContentHeight(align = Alignment.CenterVertically),
                textAlign = TextAlign.Left,
                fontWeight = FontWeight.Bold)
        }
    }
}
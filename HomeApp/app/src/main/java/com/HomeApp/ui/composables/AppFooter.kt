package com.HomeApp.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.HomeApp.R
import com.HomeApp.ui.navigation.Devices
import com.HomeApp.ui.navigation.Home
import com.HomeApp.util.microphoneIcon

@Composable
fun AppFooter(
    navController: NavController, modifier: Modifier = Modifier, bgCol: Color,
    micColor: Color = if (MaterialTheme.colors.isLight) Color.Black else Color.White
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        val rowBgCol = colorResource(id = R.color.LightSteelBlue)
        val screenWidth = LocalConfiguration.current.screenWidthDp
        val screenHeight = LocalConfiguration.current.screenHeightDp
        Box(Modifier.offset(y = (56).dp)) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .height(112.dp)
                    .background(Color.Yellow)
                    .then(modifier)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(rowBgCol)
                        .height(56.dp)
                ) {
                    // HOME
                    ButtonIcon(
                        navController,
                        Home.icon,
                        destination = Home.route,
                        description = "Home icon",
                        modifier = Modifier.width((screenWidth / 2).dp),
                        iconCol = bgCol
                    )

                    // DEVICES
                    ButtonIcon(
                        navController,
                        Devices.icon,
                        destination = Devices.route,
                        description = "Devices icon",
                        modifier = Modifier.width((screenWidth / 2).dp),
                        iconCol = bgCol
                    )
                }
                // MIC
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    ButtonIcon(
                        navController,
                        microphoneIcon,
                        "Settings icon",
                        iconScale = 1.45f,
                        modifier = Modifier
                            .offset(y = -(60).dp)
                            .padding(15.dp)
                            .zIndex(2f)
                            .background(bgCol, CircleShape),
                        iconCol = micColor
                    )
                }
            }
        }
    }
}

@Composable
private fun ButtonIcon(
    navController: NavController,
    icon: ImageVector,
    description: String,
    modifier: Modifier = Modifier,
    iconCol: Color = MaterialTheme.colors.background,
    iconScale: Float = 1f,
    destination: String? = null
) {
    val cond =
        destination != null && destination != navController.currentBackStackEntry?.destination?.route

    Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
        IconButton(
            modifier = Modifier
                .scale(1.4f)
                .zIndex(1f)
                .then(modifier),
            onClick = { if (cond) navController.navigate(destination!!) },
        ) {
            Icon(
                tint = iconCol,
                imageVector = icon,
                contentDescription = description,
                modifier = Modifier.scale(iconScale)
            )
        }
    }
}

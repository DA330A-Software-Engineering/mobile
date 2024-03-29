package com.HomeApp.ui.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomAppBar
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
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.HomeApp.ui.navigation.Devices
import com.HomeApp.ui.navigation.Home
import com.HomeApp.ui.navigation.navigateSingleTopTo
import com.HomeApp.ui.theme.LightSteelBlue

@Composable
fun AppFooter(
    navController: NavController, modifier: Modifier = Modifier, bgCol: Color,
    micColor: Color = if (MaterialTheme.colors.isLight) Color.Black else Color.White
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        val rowBgCol = LightSteelBlue
        val screenWidth = LocalConfiguration.current.screenWidthDp
        val screenHeight = LocalConfiguration.current.screenHeightDp

        BottomAppBar(
            cutoutShape = CircleShape, modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            val screenWidth = LocalConfiguration.current.screenWidthDp
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
            onClick = { if (cond) navController.navigateSingleTopTo(destination!!) },
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

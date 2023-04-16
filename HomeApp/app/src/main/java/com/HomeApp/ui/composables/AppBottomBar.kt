package com.HomeApp.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.HomeApp.ui.navigation.Devices
import com.HomeApp.ui.navigation.Home
import com.HomeApp.ui.theme.GhostWhite
import com.HomeApp.ui.theme.LightSteelBlue
import com.HomeApp.util.microphoneIcon

@Composable
fun AppBottomBar(navController: NavController, modifier: Modifier = Modifier) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        val bgCol = GhostWhite
        val screenWidth = LocalConfiguration.current.screenWidthDp
        Column(
            modifier = Modifier
                .background(bgCol)
                .fillMaxWidth()
                .height(168.dp)
                .zIndex(1f), horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(Modifier.weight(1f))
            IconButton(
                modifier = Modifier
                    .offset(y = 40.dp)
                    .background(
                        bgCol,
                        shape = RoundedCornerShape(80.dp)
                    )
                    .zIndex(2f),
                onClick = { /*TODO*/ },
            ) {
                Icon(
                    imageVector = microphoneIcon,
                    contentDescription = "Microphone Icon",
//                                tint = colorResource(id = R.color.GhostWhite),
                    modifier = Modifier
                        .scale(1.9f)
                        .padding(25.dp)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(LightSteelBlue)
                    .padding(top = 5.dp)
                    .zIndex(1f)
            ) {
                // HOME ICON
                IconButton(
                    modifier = Modifier.width((screenWidth / 2).dp),
                    onClick = {
                        if (navController.currentBackStackEntry?.destination?.route != Home.route) {
                            navController.navigate(Home.route)
                        }
                    }
                ) {
                    Icon(
                        imageVector = Home.icon,
                        contentDescription = "Home Icon",
                        tint = GhostWhite,
                        modifier = Modifier.scale(1.5f)
                    )
                }

                // DEVICES ICON
                IconButton(
                    modifier = Modifier.width((screenWidth / 2).dp),
                    onClick = {
                        if (navController.currentBackStackEntry?.destination?.route != Devices.route) {
                            navController.navigate(Devices.route)
                        }
                    }
                ) {
                    Icon(
                        imageVector = Devices.icon,
                        contentDescription = "Devices Icon",
                        tint = GhostWhite,
                        modifier = Modifier.scale(1.4f)
                    )
                }
            }
        }
    }
}

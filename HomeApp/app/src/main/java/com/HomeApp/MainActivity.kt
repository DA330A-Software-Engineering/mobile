package com.HomeApp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.HomeApp.drawers.SideDrawer
import com.HomeApp.ui.navigation.AnimatedAppNavHost
import com.HomeApp.ui.navigation.Devices
import com.HomeApp.ui.navigation.Home
import com.HomeApp.ui.theme.HomeAppTheme
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    runApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun runApp() {
    val navController = rememberAnimatedNavController()
    val state = rememberScaffoldState(rememberDrawerState(initialValue = DrawerValue.Closed))

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            scaffoldState = state,
            topBar = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                            .background(Color.LightGray)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = "Top bar content",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            },
            content = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    Box(
                        modifier = Modifier
                            .padding(it) // only applied when there is a bottomBar added.
                            // set height of padding equal to 1/x of device screen current height
                            .padding(top = (LocalConfiguration.current.screenHeightDp / 50).dp)
                    ) {
                        AnimatedAppNavHost(
                            navController = navController,
                            startDestination = Devices.route
                        )
                    }
                }
            },
            drawerShape = RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp),
            drawerContent = {/**
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr)
                {

                    val sideDrawer: SideDrawer = SideDrawer(
                        drawerState = state.drawerState,
                        navController = navController
                    )
                    sideDrawer.drawScaffold()
                }*/
            },
            drawerGesturesEnabled = state.drawerState.isOpen,
        )
    }
}

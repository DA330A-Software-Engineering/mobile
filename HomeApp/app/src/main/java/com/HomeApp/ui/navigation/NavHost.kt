package com.HomeApp.ui.navigation

import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.HomeApp.screens.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

/**
 * Docs: https://google.github.io/accompanist/navigation-animation/
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedAppNavHost(
    modifier: Modifier = Modifier, navController: NavHostController,
    startDestination: String,
    state: ScaffoldState,
    getSpeechInput: (Context) -> Unit = {}
) {
    // Calls the navigate function to control movement between views/screens in the app
    val defaultTween = 450

    AnimatedNavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // LOGIN
        composable(
            route = Login.route,
            enterTransition = { fadeIn(tween(defaultTween)) },
            popEnterTransition = { fadeIn(tween(defaultTween)) },
            exitTransition = { fadeOut(tween(defaultTween)) },
            popExitTransition = { fadeOut(tween(defaultTween)) }
        ) {
            LoginScreen(
                navController = navController,
                OnSelfClick = { navController.navigateSingleTopTo(Login.route) }
            )
        }

        // HOME
        composable(
            route = Home.route,
            enterTransition = { fadeIn(tween(defaultTween)) },
            popEnterTransition = { fadeIn(tween(defaultTween)) },
            exitTransition = { fadeOut(tween(defaultTween)) },
            popExitTransition = { fadeOut(tween(defaultTween)) }
        ) {
            HomeScreen(
                navController = navController,
                OnSelfClick = { navController.navigateSingleTopTo(Login.route) },
                state = state,
                getSpeechInput = { getSpeechInput(it) }
            )
        }

        // DEVICES
        composable(
            route = Devices.route,
            enterTransition = { fadeIn(tween(defaultTween)) },
            popEnterTransition = { fadeIn(tween(defaultTween)) },
            exitTransition = { fadeOut(tween(defaultTween)) },
            popExitTransition = { fadeOut(tween(defaultTween)) }
        ) {
            DevicesScreen(
                navController = navController,
                OnSelfClick = { navController.navigateSingleTopTo(ConfirmToken.route) },
                state = state,
                getSpeechInput = { getSpeechInput(it) }
            )
        }
        111
        // GROUPS
        composable(
            route = Groups.route,
            enterTransition = { fadeIn(tween(defaultTween)) },
            popEnterTransition = { fadeIn(tween(defaultTween)) },
            exitTransition = { fadeOut(tween(defaultTween)) },
            popExitTransition = { fadeOut(tween(defaultTween)) }
        ) {
            GroupsScreen(
                navController = navController,
                OnSelfClick = { navController.navigateSingleTopTo(ConfirmToken.route) },
            )
        }

        // ROUTINES
        composable(
            route = Routines.route,
            enterTransition = { fadeIn(tween(defaultTween)) },
            popEnterTransition = { fadeIn(tween(defaultTween)) },
            exitTransition = { fadeOut(tween(defaultTween)) },
            popExitTransition = { fadeOut(tween(defaultTween)) }
        ) {
            RoutinesScreen(
                navController = navController,
                OnSelfClick = { navController.navigateSingleTopTo(ConfirmToken.route) },
            )
        }

        // SETTINGS
        composable(
            route = Settings.route,
            enterTransition = { fadeIn(tween(defaultTween)) },
            popEnterTransition = { fadeIn(tween(defaultTween)) },
            exitTransition = { fadeOut(tween(defaultTween)) },
            popExitTransition = { fadeOut(tween(defaultTween)) }
        ) {
            SettingsScreen(
                navController = navController,
                OnSelfClick = { navController.navigateSingleTopTo(Settings.route) }
            )
        }

        // RESET PASSWORD
        composable(
            route = ResetPassword.route,
            enterTransition = { fadeIn(tween(defaultTween)) },
            popEnterTransition = { fadeIn(tween(defaultTween)) },
            exitTransition = { fadeOut(tween(defaultTween)) },
            popExitTransition = { fadeOut(tween(defaultTween)) }
        ) {
            ResetPasswordScreen(
                navController = navController,
                OnSelfClick = { navController.navigateSingleTopTo(ResetPassword.route) }
            )
        }

        // FORGOT PASSWORD
        composable(
            route = ForgotPassword.route,
            enterTransition = { fadeIn(tween(defaultTween)) },
            popEnterTransition = { fadeIn(tween(defaultTween)) },
            exitTransition = { fadeOut(tween(defaultTween)) },
            popExitTransition = { fadeOut(tween(defaultTween)) }
        ) {
            ForgotPasswordScreen(
                navController = navController,
                OnSelfClick = { navController.navigateSingleTopTo(ForgotPassword.route) }
            )
        }


        // LOGOUT
        composable(
            route = Logout.route,
            enterTransition = { fadeIn(tween(defaultTween)) },
            popEnterTransition = { fadeIn(tween(defaultTween)) },
            exitTransition = { fadeOut(tween(defaultTween)) },
            popExitTransition = { fadeOut(tween(defaultTween)) }
        ) {
            LogoutScreen(
                navController = navController,
                OnSelfClick = { navController.navigateSingleTopTo(Logout.route) }
            )
        }

        // CONFIRM TOKEN
        composable(
            route = ConfirmToken.route,
            enterTransition = { fadeIn(tween(defaultTween)) },
            popEnterTransition = { fadeIn(tween(defaultTween)) },
            exitTransition = { fadeOut(tween(defaultTween)) },
            popExitTransition = { fadeOut(tween(defaultTween)) }
        ) {
            ConfirmTokenScreen(
                navController = navController,
                OnSelfClick = { navController.navigateSingleTopTo(ConfirmToken.route) }
            )
        }

        // CREATE ACCOUNT
        composable(
            route = CreateAccount.route,
            enterTransition = { fadeIn(tween(defaultTween)) },
            popEnterTransition = { fadeIn(tween(defaultTween)) },
            exitTransition = { fadeOut(tween(defaultTween)) },
            popExitTransition = { fadeOut(tween(defaultTween)) }
        ) {
            CreateAccountScreen(
                navController = navController,
                OnSelfClick = { navController.navigateSingleTopTo(CreateAccount.route) }
            )
        }
    }
}


fun NavHostController.navigateSingleTopTo(route: String) = this.navigate(route) {
    // Pop up to the start destination of the graph to
    // avoid building up a large stack of destinations
    // on the back stack as users select items
    popUpTo(
        this@navigateSingleTopTo.graph.findStartDestination().id
    ) {
        saveState = true
    }
    launchSingleTop = true
    restoreState = true
}

fun NavController.navigateSingleTopTo(route: String) = this.navigate(route) {
    // Pop up to the start destination of the graph to
    // avoid building up a large stack of destinations
    // on the back stack as users select items
    popUpTo(
        this@navigateSingleTopTo.graph.findStartDestination().id
    ) {
        saveState = true
    }
    launchSingleTop = true
    restoreState = true
}

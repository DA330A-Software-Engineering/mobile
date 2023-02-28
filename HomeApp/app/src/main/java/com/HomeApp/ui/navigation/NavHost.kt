package com.HomeApp.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    startDestination: String
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
                OnSelfClick = { navController.navigateSingleTopTo(Login.route) }
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

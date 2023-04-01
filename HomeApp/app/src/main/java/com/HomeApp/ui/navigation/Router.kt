package com.HomeApp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Creates a routing interface through which different views may be accessed as states in the
 * navController's backstack
 */

sealed interface NavPath {
    val icon: ImageVector?
    val route: String
}

object Loading : NavPath {
    override val route = "load"
    override val icon: ImageVector = Icons.Rounded.AccessTime
}


object Login : NavPath {
    override val route = "login"
    override val icon: ImageVector = Icons.Rounded.Login
}

object Home : NavPath {
    override val route = "home"
    override val icon: ImageVector = Icons.Rounded.Home
}

object Devices : NavPath {
    override val icon = Icons.Rounded.Lightbulb
    override val route = "devices"
}

object Routines : NavPath {
    override val icon = Icons.Rounded.AccessTime
    override val route = "routines"
}

object Groups : NavPath {
    override val icon: ImageVector? = null
    override val route = "groups"
}

object Settings : NavPath {
    override val icon = Icons.Rounded.Settings
    override val route = "settings"
}

object ResetPassword : NavPath {
    override val icon = Icons.Rounded.LockReset
    override val route = "reset-password"
}

object ForgotPassword : NavPath {
    override val icon = Icons.Rounded.QuestionMark
    override val route = "forgot-password"
}

object Logout : NavPath {
    override val icon = Icons.Rounded.TaskAlt
    override val route = "logout"
}

object ConfirmToken : NavPath {
    override val icon = Icons.Rounded.DomainDisabled
    override val route = "confirm-token"
}

object CreateAccount : NavPath {
    override val icon = Icons.Rounded.Person
    override val route = "create-account"
}

package com.HomeApp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.Devices
import androidx.compose.material.icons.rounded.DomainDisabled
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.LockReset
import androidx.compose.material.icons.rounded.Login
import androidx.compose.material.icons.rounded.People
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.TaskAlt
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

object Profile : NavPath {
    override val icon = Icons.Rounded.People
    override val route = "profile"
}

object ChooseType : NavPath {
    override val icon = Icons.Rounded.Devices
    override val route = "choose-type"
}

object ChooseItems : NavPath {
    override val icon = Icons.Rounded.Devices
    override val route = "choose-items"
}

object ChooseActions : NavPath {
    override val icon = Icons.Rounded.Devices
    override val route = "choose-actions"
}

object ChooseSchedule : NavPath {
    override val icon = Icons.Rounded.Schedule
    override val route = "choose-schedule"
}

object Finish : NavPath {
    override val icon = Icons.Rounded.Schedule
    override val route = "finish"
}

object Triggers : NavPath {
    override val icon = Icons.Rounded.Schedule
    override val route = "triggers"
}

object EditTrigger : NavPath {
    override val icon = Icons.Rounded.Schedule
    override val route = "edit-trigger"
}
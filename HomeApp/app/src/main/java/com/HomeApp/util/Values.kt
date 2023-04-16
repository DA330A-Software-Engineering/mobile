package com.HomeApp.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Curtains
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.outlined.DoorFront
import androidx.compose.material.icons.rounded.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.HomeApp.ui.navigation.Home
import com.HomeApp.ui.navigation.Loading
import com.HomeApp.ui.navigation.Profile

val firebaseConfig = mapOf(
    "apiKey" to "AIzaSyBe9OfClxqevJF_7X5v2Rk1lP9EQTWv458",
    "authDomain" to "software-eng-db.firebaseapp.com",
    "databaseURL" to "https://software-eng-db-default-rtdb.europe-west1.firebasedatabase.app/",
    "projectId" to "software-eng-db",
    "storageBucket" to "software-eng-db.appspot.com",
    "messagingSenderId" to "217660507084",
    "appId" to "1:217660507084:web:ed534d48fd3b02b6c31369",
    "measurementId" to "G-ZTFWXTM71X"
)

val microphoneIcon: ImageVector = Icons.Rounded.Mic

enum class SideBarOptions(val title: String, val icon: ImageVector?, val route: String) {
    PROFILE("Profile & Family", Icons.Rounded.People, Profile.route),
    NOTIFICATIONS("Notifications", Icons.Rounded.Notifications, Home.route),
    History("History", Icons.Rounded.History, Home.route),
    LOGOUT("Logout", Icons.Rounded.Logout, Loading.route),
}

val enableTopDrawer: Boolean = false

enum class DevicesFilters(
    val filterName: String,
    val filterIcon: ImageVector,
    val filterValue: String
) {
    LIGHTS(filterName = "Lights", filterIcon = Icons.Filled.Lightbulb, filterValue = "lights"),
    CURTAINS(filterName = "Curtains", filterIcon = Icons.Filled.Curtains, filterValue = "curtains"),
    DOORS(filterName = "Doors", filterIcon = Icons.Outlined.DoorFront, filterValue = "doors")
}

val AUTH_TOKEN_NAME = "x-auth-header"
val DB_ADDR = "http://10.0.2.2:3000"

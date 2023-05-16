package com.HomeApp.screens

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.HomeApp.util.getEmailFromToken

data class Profile(
    var email: String = ""
)

@Composable
fun ProfileScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    OnSelfClick: () -> Unit = {}
) {
    val context: Context = LocalContext.current
    val profile = Profile(getEmailFromToken(context))
}

package com.HomeApp.ui.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.HomeApp.ui.navigation.CreateAccount
import com.HomeApp.ui.navigation.Login

@Composable
fun BottomDivider(
    divider: Divider,
    navController: NavController
) {
    Divider(
        Modifier
            .padding(top = divider.padding.dp)
            .padding(bottom = 15.dp),
        color = Color.Black.copy(alpha = 0.3f),
        thickness = 1.dp,
    )
    Row(
        Modifier.padding(bottom = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(divider.text, color = Color.Black)
        TextButton(onClick = { navController.navigate(divider.onClick) }) {
            Text(divider.textButton)
        }
    }
}

sealed class Divider(
    val text: String,
    val textButton: String,
    val onClick: String,
    val padding: Int
) {
    object SignUp : Divider(
        text = "Don't have an account?",
        textButton = "Sign up",
        onClick = CreateAccount.route,
        padding = 124
    )

    object SignIn : Divider(
        text = "Already have an account?",
        textButton = "Sign in",
        onClick = Login.route,
        padding = 188
    )
}
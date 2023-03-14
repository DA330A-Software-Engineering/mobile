package com.HomeApp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = GhostWhite,
    primaryVariant = Color.DarkGray,
    secondary = Teal200

)

private val LightColorPalette = lightColors(
    primary = LightSteelBlue,
    primaryVariant = RaminGrey,
    secondary = LightSteelBlue,
    background = GhostWhite,
//    onPrimary = Color.Black,
//    onSecondary = Color.LightGray,

    /* Other default colors to override
    surface = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun HomeAppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

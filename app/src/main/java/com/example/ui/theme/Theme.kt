package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = NeonBlue,
    secondary = NeonPurple,
    tertiary = CyberMagenta,
    background = CyberBackground,
    surface = CyberSurface,
    onPrimary = PureBlack,
    onSecondary = TextWhite,
    onBackground = TextWhite,
    onSurface = TextWhite
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Force cyberpunk dark theme
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}

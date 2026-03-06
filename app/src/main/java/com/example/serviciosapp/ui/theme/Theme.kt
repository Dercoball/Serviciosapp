package com.example.serviciosapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Sky400,
    onPrimary = Ink900,
    secondary = Mint500,
    background = Ink900,
    surface = Ink500,
    surfaceVariant = Ink700,
    onSurface = Cloud50,
    onSurfaceVariant = Cloud100
)

private val LightColorScheme = lightColorScheme(
    primary = Ink700,
    onPrimary = Cloud50,
    secondary = Sky600,
    onSecondary = Cloud50,
    tertiary = Mint500,
    background = Cloud50,
    surface = Cloud50,
    surfaceVariant = Cloud100,
    onSurface = Ink500,
    onSurfaceVariant = Color(0xFF475569)
)

@Composable
fun ServiciosAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

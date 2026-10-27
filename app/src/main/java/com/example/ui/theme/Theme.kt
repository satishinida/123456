package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Consistent Luxury Warm Off-White Theme
private val RentoraWarmColorScheme = lightColorScheme(
    primary = RentoraCoral,
    onPrimary = Color.White,
    primaryContainer = RentoraCoralLight,
    onPrimaryContainer = RentoraCoralDark,
    secondary = RentoraTeal,
    onSecondary = Color.White,
    background = RentoraBackground,
    onBackground = RentoraCharcoal,
    surface = RentoraCard,
    onSurface = RentoraCharcoal,
    surfaceVariant = RentoraSurface,
    onSurfaceVariant = RentoraMuted,
    outline = RentoraBorder
)

@Composable
fun RentoraTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = RentoraWarmColorScheme,
        typography = Typography,
        content = content
    )
}

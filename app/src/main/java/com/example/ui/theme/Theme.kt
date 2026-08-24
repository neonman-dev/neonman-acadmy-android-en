package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val NeonDarkColorScheme = darkColorScheme(
    primary = NeonCyan,
    secondary = NeonViolet,
    tertiary = NeonCyan,
    background = DarkBackground,
    surface = DarkElevatedSurface,
    surfaceVariant = DarkCardPanel,
    onPrimary = DarkBackground,
    onSecondary = Color.White,
    onBackground = DarkTextPrimary,
    onSurface = DarkTextPrimary,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkBorder,
    error = NeonDanger
)

private val SketchLightColorScheme = lightColorScheme(
    primary = SketchBorder,
    secondary = SketchBorder,
    tertiary = SketchBorder,
    background = SketchBackground,
    surface = SketchSurface,
    surfaceVariant = SketchBackground,
    onPrimary = Color.White,
    onSecondary = SketchTextPrimary,
    onBackground = SketchTextPrimary,
    onSurface = SketchTextPrimary,
    onSurfaceVariant = SketchTextSecondary,
    outline = SketchBorder,
    error = NeonDanger
)

data class ExtraThemeColors(
    val isDark: Boolean,
    val cardBackground: Color,
    val cardBorder: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val accentCyan: Color,
    val accentViolet: Color
)

val LocalExtraThemeColors = staticCompositionLocalOf {
    ExtraThemeColors(
        isDark = true,
        cardBackground = DarkCardPanel,
        cardBorder = DarkBorder,
        textPrimary = DarkTextPrimary,
        textSecondary = DarkTextSecondary,
        accentCyan = NeonCyan,
        accentViolet = NeonViolet
    )
}

@Composable
fun NeonmanTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) NeonDarkColorScheme else SketchLightColorScheme

    val extraColors = if (darkTheme) {
        ExtraThemeColors(
            isDark = true,
            cardBackground = DarkCardPanel,
            cardBorder = DarkBorder,
            textPrimary = DarkTextPrimary,
            textSecondary = DarkTextSecondary,
            accentCyan = NeonCyan,
            accentViolet = NeonViolet
        )
    } else {
        ExtraThemeColors(
            isDark = false,
            cardBackground = SketchBackground,
            cardBorder = SketchBorder,
            textPrimary = SketchTextPrimary,
            textSecondary = SketchTextSecondary,
            accentCyan = SketchBorder,
            accentViolet = SketchBorder
        )
    }

    CompositionLocalProvider(LocalExtraThemeColors provides extraColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

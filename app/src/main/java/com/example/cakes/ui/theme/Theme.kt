package com.example.cakes.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(

    primary = CakeCoral,
    onPrimary = Color.White,

    primaryContainer = CakeCoralLight,
    onPrimaryContainer = CakeTextPrimary,

    secondary = Color(0xFFD99A82),
    onSecondary = Color.White,

    secondaryContainer = Color(0xFFFFE3DA),
    onSecondaryContainer = CakeTextPrimary,

    tertiary = CakeYellow,
    onTertiary = CakeTextPrimary,

    background = CakeBackground,
    onBackground = CakeTextPrimary,

    surface = CakeSurface,
    onSurface = CakeTextPrimary,

    surfaceVariant = CakeSurfaceSoft,
    onSurfaceVariant = CakeTextSecondary,

    outline = CakeOutline
)

private val DarkColorScheme = darkColorScheme(

    primary = CakeDarkPrimary,
    onPrimary = Color(0xFF5C1A0F),

    primaryContainer = CakeCoralDark,
    onPrimaryContainer = Color.White,

    secondary = Color(0xFFE7AE99),

    background = CakeDarkBackground,
    onBackground = Color(0xFFF5E9E5),

    surface = CakeDarkSurface,
    onSurface = Color(0xFFF5E9E5),

    surfaceVariant = Color(0xFF3A312E),
    onSurfaceVariant = Color(0xFFD8C7C1),

    outline = Color(0xFF8F7D77)
)

@Composable
fun CakeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    val view = LocalView.current

    if (!view.isInEditMode) {
        val window = (view.context as Activity).window

        WindowCompat.getInsetsController(window, view).apply {
            isAppearanceLightStatusBars = !darkTheme
            isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = CakeTypography,
        shapes = CakeShapes,
        content = content
    )
}
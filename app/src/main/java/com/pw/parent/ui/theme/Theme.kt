package com.pw.parent.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import android.app.Activity

private val DarkColorScheme = darkColorScheme(
    primary = ParentAppPrimary,
    secondary = ParentAppSecondary,
    tertiary = ParentAppAccent,
    background = ParentAppDarkBackground,
    surface = ParentAppSurfaceDark,
    surfaceVariant = ParentAppSurfaceCard,
    onPrimary = ParentAppDarkBackground,
    onSecondary = ParentAppDarkBackground,
    onBackground = ParentAppLightBackground,
    onSurface = ParentAppLightBackground
)

private val LightColorScheme = lightColorScheme(
    primary = ParentAppPrimaryLight,
    secondary = ParentAppSecondaryLight,
    tertiary = ParentAppAccent,
    background = ParentAppLightBackground,
    surface = ParentAppSurfaceLight,
    surfaceVariant = ParentAppSurfaceCardLight,
    onPrimary = ParentAppSurfaceLight,
    onSecondary = ParentAppSurfaceLight,
    onBackground = ParentAppDarkBackground,
    onSurface = ParentAppDarkBackground
)

@Composable
fun ParentAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Allow dynamic colors on API 31+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        val window = (view.context as? Activity)?.window
        if (window != null) {
            SideEffect {
                val insetsController = WindowCompat.getInsetsController(window, view)
                insetsController.isAppearanceLightStatusBars = !darkTheme
                insetsController.isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

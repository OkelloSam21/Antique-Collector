package com.example.antiquecollector.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.antiquecollector.ui.theme.atoms.getAntiqueScheme
import com.example.antiquecollector.ui.theme.atoms.getColorScheme
import com.example.antiquecollector.ui.theme.atoms.primaryDark
import com.example.antiquecollector.ui.theme.atoms.primaryLight


@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color
)

val unspecified_scheme = ColorFamily(
    Color.Unspecified, Color.Unspecified, Color.Unspecified, Color.Unspecified
)

@Composable
fun AntiqueCollectorTheme(
    isDarkThemeEnabled: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable() () -> Unit
) {
    val colorScheme = getColorScheme(darkTheme = isDarkThemeEnabled)
    val appTheme = getAntiqueScheme(isDarkThemeEnabled)
    val view = LocalView.current
    val context = LocalContext.current
    // Get primary color for status bar based on dark/light theme
    var statusBarColor = if (isDarkThemeEnabled) primaryDark else primaryLight

    if (!view.isInEditMode) {
        SideEffect {
            val window = (context as Activity).window
            val insetsController = WindowCompat.getInsetsController(window, view)

            // Configure the behavior of the status bar
            insetsController.apply {
                systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                // Set status bar color
                window.statusBarColor = statusBarColor.toArgb()

                // Set status bar appearance based on theme (light or dark icons)
                isAppearanceLightStatusBars = !isDarkThemeEnabled
            }

            // Configure the behavior of the navigation bar (if needed and on supported API levels)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                insetsController.apply {
                    // Set navigation bar color
                    window.navigationBarColor = colorScheme.background.toArgb()

                    // Set navigation bar appearance based on theme (light or dark icons)
                    isAppearanceLightNavigationBars = !isDarkThemeEnabled
                }
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
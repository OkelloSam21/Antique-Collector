package com.example.antiquecollector.ui.theme.helper

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.example.antiquecollector.ui.theme.atoms.onPrimaryDark
import com.example.antiquecollector.ui.theme.atoms.onPrimaryLight
import com.example.antiquecollector.ui.theme.atoms.onSecondaryContainerDark
import com.example.antiquecollector.ui.theme.atoms.onSecondaryContainerLight
import com.example.antiquecollector.ui.theme.atoms.onTertiaryContainerDark
import com.example.antiquecollector.ui.theme.atoms.onTertiaryContainerLight
import com.example.antiquecollector.ui.theme.atoms.primaryDark
import com.example.antiquecollector.ui.theme.atoms.primaryLight
import com.example.antiquecollector.ui.theme.atoms.secondaryContainerDark
import com.example.antiquecollector.ui.theme.atoms.secondaryContainerLight
import com.example.antiquecollector.ui.theme.atoms.tertiaryContainerDark
import com.example.antiquecollector.ui.theme.atoms.tertiaryContainerLight


@Immutable
data class AntiqueColorScheme internal constructor(
    val success: Color = Color.Green, // Example, replace with your actual color
    val onSuccess: Color = Color.White, // Example
    val info: Color = Color.Blue, // Example
    val onInfo: Color = Color.White, // Example
    val pastelSurface: List<Color> = listOf(
        Color(0xFFE0BBE4),
        Color(0xFF957DAD),
        Color(0xFFD291BC)
    ), // Example
)

internal val AntiqueLightColorScheme =
    AntiqueColorScheme(
        success = primaryLight,
        onSuccess = onPrimaryLight,
        pastelSurface = listOf(
            tertiaryContainerLight,
            onTertiaryContainerLight,
            secondaryContainerLight,
            onSecondaryContainerLight
        ),
        info = secondaryContainerLight,
        onInfo = onSecondaryContainerLight,
    )

internal val AntiqueDarkColorScheme =
    AntiqueColorScheme(
        success = primaryDark,
        onSuccess = onPrimaryDark,
        pastelSurface = listOf(
            tertiaryContainerDark,
            onTertiaryContainerDark,
            secondaryContainerDark,
            onSecondaryContainerDark
        ),
        info = secondaryContainerDark,
        onInfo = onSecondaryContainerDark,
    )

internal val LocalSafiColorScheme = staticCompositionLocalOf { AntiqueColorScheme() }

val ColorScheme.success: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalSafiColorScheme.current.success

val ColorScheme.onSuccess: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalSafiColorScheme.current.onSuccess

val ColorScheme.pastelSurface: List<Color>
    @Composable
    @ReadOnlyComposable
    get() = LocalSafiColorScheme.current.pastelSurface

val ColorScheme.info: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalSafiColorScheme.current.info

val ColorScheme.onInfo: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalSafiColorScheme.current.onInfo

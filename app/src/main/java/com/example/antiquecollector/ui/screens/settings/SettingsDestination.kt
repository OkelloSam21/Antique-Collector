package com.example.antiquecollector.ui.screens.settings

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object SettingsDestination


fun NavGraphBuilder.settings(
    onNavigateUp: () -> Unit
) {
    composable<SettingsDestination> {
        SettingsScreen(
            onNavigateUp = onNavigateUp
        )
    }
}

fun NavController.navigateToSettings() {
    this.navigate(SettingsDestination)
}
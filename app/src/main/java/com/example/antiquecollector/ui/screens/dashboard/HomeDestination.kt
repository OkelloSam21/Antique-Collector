package com.example.antiquecollector.ui.screens.dashboard

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object HomeDestination

fun NavGraphBuilder.homeScreen() {
    composable<HomeDestination> {
        HomeScreen()
    }
}
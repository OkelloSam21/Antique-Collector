package com.example.antiquecollector.ui.screens.explore

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object ExploreDestination

fun NavGraphBuilder.explore() {
    composable<ExploreDestination> {
        ExploreScreen()
    }
}

fun NavController.navigateToExplore() {
    this.navigate(ExploreDestination)
}
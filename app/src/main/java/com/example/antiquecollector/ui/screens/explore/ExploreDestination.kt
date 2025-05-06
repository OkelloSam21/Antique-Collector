package com.example.antiquecollector.ui.screens.explore

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.antiquecollector.ui.screens.home.HomeDestination
import com.example.antiquecollector.util.ArtifactId
import kotlinx.serialization.Serializable

object ExploreDestination {
    const val route = "explore"
}

fun NavGraphBuilder.exploreScreen(
    onNavigateToDetail: (ArtifactId) -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToCategory: (Long) -> Unit
) {
    composable(route = ExploreDestination.route) {
        ExploreScreen(
            onNavigateToArtifactDetail = onNavigateToDetail,
            onNavigateToSearch = onNavigateToSearch,
            onNavigateToCategory = onNavigateToCategory
        )
    }
}

fun NavController.navigateToExplore() {
    this.navigate(ExploreDestination.route) {
        popUpTo(HomeDestination)
        launchSingleTop = true
    }
}
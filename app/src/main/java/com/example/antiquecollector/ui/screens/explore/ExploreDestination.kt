package com.example.antiquecollector.ui.screens.explore

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.antiquecollector.ui.screens.home.HomeDestination
import com.example.antiquecollector.util.ArtifactId
import kotlinx.serialization.Serializable

// First, define the destination object
object ExploreDestination {
    const val route = "explore"
}

// Then add the composable to your NavGraphBuilder
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

// Add helper extension function for navigation
fun NavController.navigateToExplore() {
    this.navigate(ExploreDestination.route) {
        // Optional: Pop up to the home route to avoid building up a large stack
        popUpTo(HomeDestination)
        // Optional: Set other navigation options like launchSingleTop
        launchSingleTop = true
    }
}
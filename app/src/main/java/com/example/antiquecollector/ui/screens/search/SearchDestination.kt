package com.example.antiquecollector.ui.screens.search

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.antiquecollector.util.ArtifactId

enum class SearchSourceType {
    LOCAL, REMOTE, BOTH
}

object SearchDestination {
    const val route = "search/{sourceType}"

    fun createRoute(sourceType: SearchSourceType = SearchSourceType.BOTH): String {
        return "search/${sourceType.name}"
    }
}

fun NavGraphBuilder.searchScreen(
    onNavigateBack: () -> Unit,
    onNavigateToArtifactDetail: (ArtifactId) -> Unit
) {
    composable(
        route = SearchDestination.route,
        arguments = listOf(
            navArgument("sourceType") { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val sourceType = when (backStackEntry.arguments?.getString("sourceType")) {
            "LOCAL" -> SearchSourceType.LOCAL
            "REMOTE" -> SearchSourceType.REMOTE
            else -> SearchSourceType.BOTH
        }

        SearchScreen(
            onNavigateBack = onNavigateBack,
            onNavigateToArtifactDetail = onNavigateToArtifactDetail,
            sourceType = sourceType
        )
    }
}

fun NavController.navigateToSearch(
    sourceType: SearchSourceType = SearchSourceType.BOTH,
    navOptions: NavOptions? = null
) {
    navigate(SearchDestination.createRoute(sourceType), navOptions)
}

fun NavController.navigateToLocalSearch() {
    navigateToSearch(SearchSourceType.LOCAL)
}

fun NavController.navigateToRemoteSearch() {
    navigateToSearch(SearchSourceType.REMOTE)
}
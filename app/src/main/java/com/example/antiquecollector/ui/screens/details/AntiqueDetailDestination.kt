package com.example.antiquecollector.ui.screens.details

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument


// First, define the destination object
object AntiqueDetailDestination {
    const val route = "antique_detail/{antiqueId}"
    const val argAntiqueId = "antiqueId"

    fun createRoute(antiqueId: String): String {
        return "antique_detail/$antiqueId"
    }
}

// Then add the composable to your NavGraphBuilder
fun NavGraphBuilder.antiqueDetailScreen(
    onNavigateUp: () -> Unit,
    onShareAntique: () -> Unit,
    onEditAntique: (String) -> Unit
) {
    composable(
        route = AntiqueDetailDestination.route,
        arguments = listOf(
            navArgument(AntiqueDetailDestination.argAntiqueId) {
                type = NavType.StringType
            }
        )
    ) {
        AntiqueDetailScreen(
            onNavigateUp = onNavigateUp,
            onShareAntique = onShareAntique,
            onEditAntique = onEditAntique
        )
    }
}

// Add helper extension function for navigation
fun NavController.navigateToAntiqueDetail(antiqueId: String) {
    this.navigate(AntiqueDetailDestination.createRoute(antiqueId))
}
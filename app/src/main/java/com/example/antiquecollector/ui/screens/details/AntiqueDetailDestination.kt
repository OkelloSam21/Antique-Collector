package com.example.antiquecollector.ui.screens.details

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.antiquecollector.util.ArtifactId

// First, define the destination object
object AntiqueDetailDestination {
    const val route = "antique_detail/{artifactId}"
    const val argArtifactId = "artifactId"

    fun createRoute(artifactId: String): String {
        return "antique_detail/$artifactId"
    }
}

fun NavGraphBuilder.antiqueDetailScreen(
    onNavigateUp: () -> Unit,
    onShareAntique: () -> Unit,
    onEditAntique: (String) -> Unit
) {
    composable(
        route = AntiqueDetailDestination.route,
        arguments = listOf(
            navArgument(AntiqueDetailDestination.argArtifactId) {
                type = NavType.StringType
            }
        )
    ) { backStackEntry ->
        val artifactIdString = backStackEntry.arguments?.getString(AntiqueDetailDestination.argArtifactId)
        Log.d("DetailDestination", "Received artifactId string: $artifactIdString")

        val artifactId = try {
            // Use the companion method for deserialization
            artifactIdString?.let { ArtifactId.deserialize(it) }
        } catch (e: Exception) {
            Log.e("DetailDestination", "Error deserializing artifactId: $e")
            null
        }

        Log.d("DetailDestination", "Deserialized artifactId: $artifactId")
        if (artifactId != null) {
            AntiqueDetailScreen(
                artifactId = artifactId,
                onNavigateUp = onNavigateUp,
                onShareAntique = onShareAntique,
                onEditAntique = onEditAntique
            )
        } else {
            // Handle invalid or missing artifactId
            // Maybe show an error screen or navigate back
            Log.e("DetailDestination", "Invalid artifactId, unable to show detail screen")
            onNavigateUp()
        }
    }
}

fun NavController.navigateToAntiqueDetail(artifactId: ArtifactId) {
    // Use the companion method for serialization
    val artifactIdJson = ArtifactId.serialize(artifactId)
    this.navigate(AntiqueDetailDestination.createRoute(artifactIdJson))
}
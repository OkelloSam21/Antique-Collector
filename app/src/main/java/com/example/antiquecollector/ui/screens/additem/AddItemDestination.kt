package com.example.antiquecollector.ui.screens.additem

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object AddItemDestination

fun NavGraphBuilder.addItemScreen(
    onNavigateUp: () -> Unit,
    onSaveComplete: () -> Unit
) {
    composable <AddItemDestination>{
        AddItemScreen(
            onNavigateUp = onNavigateUp,
            onSaveComplete = onSaveComplete
        )
    }
}

fun NavController.navigateToAddItem() {
    this.navigate(AddItemDestination)
}
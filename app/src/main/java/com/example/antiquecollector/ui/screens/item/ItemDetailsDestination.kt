package com.example.antiquecollector.ui.screens.item

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object ItemDetailsDestination

fun NavGraphBuilder.itemDetails() {
    composable<ItemDetailsDestination> {
        ItemDetailsScreen()
    }
}

fun NavController.navigateToItemDetails() {
    this.navigate(ItemDetailsDestination)
}
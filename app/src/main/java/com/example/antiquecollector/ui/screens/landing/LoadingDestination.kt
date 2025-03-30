package com.example.antiquecollector.ui.screens.landing

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object LoadingDestination

fun NavGraphBuilder.loadingScreen(
    content: @Composable () -> Unit ={}
) {
    composable <LoadingDestination> {
        LoadingScreen()
        content()
    }
}
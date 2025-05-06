package com.example.antiquecollector.ui.screens.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.antiquecollector.ui.screens.search.navigateToLocalSearch
import com.example.antiquecollector.util.ArtifactId
import com.example.antiquecollector.util.CurrencyFormatter
import com.example.antiquecollector.util.DateUtils
import kotlinx.serialization.Serializable

@Serializable
data object HomeDestination

fun NavGraphBuilder.homeScreen(
    onNavigateToDetail: (ArtifactId) -> Unit,
    onNavigateToCategory: (Long) -> Unit,
    onNavigateToExplore: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToAddItem: () -> Unit,
    onNavigateToSearch: () -> Unit,
    currencyFormatter: CurrencyFormatter,
    dateUtils: DateUtils,
) {
    composable<HomeDestination> {
        HomeScreen(
            onNavigateToDetail = onNavigateToDetail,
            onNavigateToCategory = onNavigateToCategory,
            onNavigateToExplore = onNavigateToExplore,
            onNavigateToSettings = onNavigateToSettings,
            onNavigateToAddItem = onNavigateToAddItem,
            onNavigateToSearch = onNavigateToSearch,
            currencyFormatter = currencyFormatter,
            dateUtils = dateUtils,
        )
    }
}

fun NavController.navigateToHome() {
    navigate(HomeDestination) {
        popUpTo(0) {
            inclusive = true
        }
    }
}
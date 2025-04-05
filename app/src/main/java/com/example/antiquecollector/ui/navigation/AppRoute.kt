package com.example.antiquecollector.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.antiquecollector.ui.screens.additem.addItemScreen
import com.example.antiquecollector.ui.screens.additem.navigateToAddItem
import com.example.antiquecollector.ui.screens.details.antiqueDetailScreen
import com.example.antiquecollector.ui.screens.details.navigateToAntiqueDetail
import com.example.antiquecollector.ui.screens.explore.exploreScreen
import com.example.antiquecollector.ui.screens.explore.navigateToExplore
import com.example.antiquecollector.ui.screens.home.homeScreen
import com.example.antiquecollector.ui.screens.home.navigateToHome
import com.example.antiquecollector.ui.screens.landing.LoadingDestination
import com.example.antiquecollector.ui.screens.landing.loadingScreen
import com.example.antiquecollector.ui.screens.onboarding.OnboardingPreferences
import com.example.antiquecollector.ui.screens.onboarding.navigateToOnboarding
import com.example.antiquecollector.ui.screens.onboarding.onBoardingScreen
import com.example.antiquecollector.ui.screens.settings.navigateToSettings
import com.example.antiquecollector.ui.screens.settings.settings
import com.example.antiquecollector.util.CurrencyFormatter
import com.example.antiquecollector.util.DateUtils

@Composable
fun AppRoute(modifier: Modifier = Modifier, onboardingPreferences: OnboardingPreferences) {
    val navController = rememberNavController()
    val onboardingComplete by onboardingPreferences.onboardingCompleteFlow.collectAsState(initial = null)

    NavHost(
        navController = navController,
        startDestination = LoadingDestination,
        modifier = modifier
    ) {
        // Loading screen with navigation logic inside
        loadingScreen{
            LaunchedEffect(onboardingComplete) {
                if (onboardingComplete == true) {
                    navController.navigateToHome()
                } else {
                    navController.navigateToOnboarding()
                }
            }
        }

        // Onboarding screen with completion callback
        onBoardingScreen(
            onOnboardingComplete = {
                navController.navigateToHome()
            }
        )

        // Home screen with various navigation callbacks
        homeScreen(
            onNavigateToDetail = { itemId ->
                // navController.navigateToDetail(itemId)
                navController.navigateToAntiqueDetail(itemId)
            },
            onNavigateToCategory = { categoryId ->
                // navController.navigateToCategory(categoryId)
            },
            onNavigateToExplore = {
                 navController.navigateToExplore()
            },
            onNavigateToSettings = {
                 navController.navigateToSettings()
            },
            onSearchClick = {
                // navController.navigateToSearch()
            },
            onNavigateToAddItem = {
                navController.navigateToAddItem()
            },
            currencyFormatter = CurrencyFormatter(),
            dateUtils = DateUtils(),
        )

        settings(
            onNavigateUp = {
                navController.popBackStack()
            }
        )

        addItemScreen(
            onSaveComplete = {
                navController.navigateToHome()
            },
            onNavigateUp = {
                navController.navigateUp()
            }
        )

        exploreScreen(
            onNavigateToDetail = {
                navController.navigateToAntiqueDetail(it)
            },
            onNavigateToSearch = {},
            onNavigateToCategory = {}
        )

        antiqueDetailScreen(
            onNavigateUp = {
                navController.popBackStack()
            },
            onShareAntique = {},
            onEditAntique = {
                navController.navigateToAntiqueDetail(it)
            }
        )
    }
}
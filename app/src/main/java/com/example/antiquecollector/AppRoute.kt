package com.example.antiquecollector

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.antiquecollector.ui.screens.dashboard.HomeDestination
import com.example.antiquecollector.ui.screens.dashboard.homeScreen
import com.example.antiquecollector.ui.screens.onboarding.OnBoardingDestination
import com.example.antiquecollector.ui.screens.onboarding.onBoardingScreen

@Composable
fun AppRoute(modifier: Modifier = Modifier) {

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = OnBoardingDestination,
        modifier = modifier
    ) {
        onBoardingScreen (
            onOnboardingComplete = {
                navController.navigate(HomeDestination)
            }
        )

        homeScreen()
    }
}
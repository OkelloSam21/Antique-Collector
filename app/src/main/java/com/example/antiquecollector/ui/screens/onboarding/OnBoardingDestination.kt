package com.example.antiquecollector.ui.screens.onboarding

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object OnBoardingDestination

fun NavGraphBuilder.onBoardingScreen(
    onOnboardingComplete: () -> Unit
) {
    composable<OnBoardingDestination> {
        OnboardingScreen(
            onOnboardingComplete = onOnboardingComplete
        )
    }
}
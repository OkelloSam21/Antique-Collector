package com.example.antiquecollector.ui.screens.onboarding

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.antiquecollector.ui.screens.landing.LoadingDestination
import kotlinx.serialization.Serializable

@Serializable
data object OnBoardingDestination

fun NavGraphBuilder.onBoardingScreen(
    onOnboardingComplete: () -> Unit
) {
    composable<OnBoardingDestination> {
        Log.d("Navigation", "Adding route for OnBoardingDestination")
        OnboardingScreen(
            onOnboardingComplete = onOnboardingComplete
        )
    }
}

fun NavController.navigateToOnboarding() {
    navigate(OnBoardingDestination) {
        popUpTo(LoadingDestination){
            inclusive = true
        }
    }
}
package com.example.antiquecollector.util

import android.app.Activity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.antiquecollector.ui.screens.onboarding.OnboardingPreferences
import kotlinx.coroutines.CoroutineScope

class SplashScreenManager(
    private val activity: Activity,
    private val onboardingPreferences: OnboardingPreferences,
    private val coroutineScope: CoroutineScope
) {
    private val splashScreen = activity.installSplashScreen()

    init {
        splashScreen.setKeepOnScreenCondition {
            onboardingPreferences.onboardingCompleteFlow.value == null
        }
    }
}
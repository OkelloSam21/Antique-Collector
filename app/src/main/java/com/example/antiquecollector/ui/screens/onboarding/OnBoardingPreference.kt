// OnboardingPreferences.kt (refactored)
package com.example.antiquecollector.ui.screens.onboarding

import androidx.datastore.preferences.core.booleanPreferencesKey
import com.example.antiquecollector.di.ApplicationScope
import com.example.antiquecollector.util.PreferencesManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OnboardingPreferences @Inject constructor(
    private val preferencesManager: PreferencesManager,
    @ApplicationScope private val coroutineScope: CoroutineScope // Inject a CoroutineScope
) {
    private val hasCompletedOnboardingKey = booleanPreferencesKey("has_completed_onboarding")

    val onboardingCompleteFlow: StateFlow<Boolean> = preferencesManager.getPreference<Boolean>( // Explicitly specify Boolean
        hasCompletedOnboardingKey,
        false
    ).stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )

    suspend fun setOnboardingCompleted(completed: Boolean) {
        preferencesManager.setPreference(hasCompletedOnboardingKey, completed)
    }
}
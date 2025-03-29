package com.example.antiquecollector.ui.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Data class representing each onboarding page
 */
data class OnboardingPage(
    val imageResId: Int,
    val title: String,
    val description: String
)

/**
 * ViewModel for managing the onboarding flow
 */
@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val onboardingPreferences: OnboardingPreferences
) : ViewModel() {

    private val _currentPage = MutableStateFlow(0)
    val currentPage: StateFlow<Int> = _currentPage

    private val _hasCompletedOnboarding = MutableStateFlow(false)
    val hasCompletedOnboarding: StateFlow<Boolean> = _hasCompletedOnboarding

    init {
        viewModelScope.launch {
            _hasCompletedOnboarding.value = onboardingPreferences.hasCompletedOnboarding()
        }
    }

    fun goToNextPage() {
        _currentPage.value += 1
    }

    fun goToPreviousPage() {
        if (_currentPage.value > 0) {
            _currentPage.value -= 1
        }
    }

    fun setCurrentPage(page: Int) {
        _currentPage.value = page
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            onboardingPreferences.setOnboardingCompleted()
            _hasCompletedOnboarding.value = true
        }
    }

    fun skipOnboarding() {
        viewModelScope.launch {
            onboardingPreferences.setOnboardingCompleted()
            _hasCompletedOnboarding.value = true
        }
    }
}
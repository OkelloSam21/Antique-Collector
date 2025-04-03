package com.example.antiquecollector.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antiquecollector.domain.model.Antique
import com.example.antiquecollector.domain.model.Category
import com.example.antiquecollector.domain.model.CollectionStatistics
import com.example.antiquecollector.domain.usecase.antique.AntiqueUseCases
import com.example.antiquecollector.domain.usecase.category.CategoryUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomedUiState(
    val isLoading: Boolean = true,
    val statistics: CollectionStatistics = CollectionStatistics(),
    val categories: List<Category> = emptyList(),
    val recentAntiques: List<Antique> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val antiqueUseCases: AntiqueUseCases,
    private val categoryUseCases: CategoryUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomedUiState())
    val uiState: StateFlow<HomedUiState> = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        _uiState.update { it.copy(isLoading = true) } // Set loading at the beginning
        val initialStatsJob = viewModelScope.launch {
            try {
                antiqueUseCases.getCollectionStatisticsUseCase().collect { stats ->
                    _uiState.update {
                        it.copy(
                            statistics = stats,
                            recentAntiques = stats.recentAdditions,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = e.message ?: "An unexpected error occurred loading statistics"
                    )
                }
            }
        }

        val categoriesJob = viewModelScope.launch {
            try {
                categoryUseCases.getCategories().collect { categories ->
                    Log.d("HomeViewModel", "Loaded categories: $categories")
                    _uiState.update { it.copy(
                        isLoading = false,categories = categories) }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = e.message ?: "An unexpected error occurred loading categories",
                        isLoading = false
                    )
                }
            }
        }

        // Set isLoading to false after both initial stats and categories are loaded.
        viewModelScope.launch {
            initialStatsJob.join() // Wait for initial statistics load
            categoriesJob.join()     // Wait for initial categories load
            _uiState.update { it.copy(isLoading = false) } // Then set loading false
        }
    }

    fun refreshData() {
        loadDashboardData()
    }
}
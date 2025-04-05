package com.example.antiquecollector.ui.screens.explore

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antiquecollector.domain.model.Category
import com.example.antiquecollector.domain.model.MuseumArtifact
import com.example.antiquecollector.domain.usecase.category.CategoryUseCases
import com.example.antiquecollector.domain.usecase.museum.MuseumUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val museumUseCases: MuseumUseCases,
    private val categoryUseCases: CategoryUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExploreUiState())
    val uiState: StateFlow<ExploreUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    /**
     * Load all data needed for the explore screen
     */
    private fun loadData() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                // Launch data loading in parallel
                launch { loadFeaturedCollection() }
                launch { loadPopularArtifacts() }
                launch { loadCategories() }
            } catch (e: Exception) {
                Log.e("ExploreViewModel", "Error loading data", e)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "An unexpected error occurred"
                    )
                }
            }
        }
    }

    /**
     * Public method to refresh all data (used by pull-to-refresh)
     */
    fun refreshData() {
        _uiState.update { it.copy(isRefreshing = true) }
        loadData()
    }

    private suspend fun loadFeaturedCollection() {
        try {
            // Load featured artifacts with period-specific queries
            val featuredQueries = listOf(
                "greek vase",
                "egyptian statue",
                "roman sculpture"
            )

            val featuredArtifacts = mutableListOf<MuseumArtifact>()

            for (query in featuredQueries) {
                val artifacts = museumUseCases.getMuseumArtifacts(query)
                if (artifacts.isNotEmpty()) {
                    // Take just the first result for each query
                    featuredArtifacts.add(artifacts.first())
                }

                // If we have enough featured artifacts, break
                if (featuredArtifacts.size >= 3) {
                    break
                }
            }

            _uiState.update {
                it.copy(
                    isLoading = false,
                    isRefreshing = false,
                    featuredArtifacts = featuredArtifacts
                )
            }
        } catch (e: Exception) {
            Log.e("ExploreViewModel", "Error loading featured collection", e)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isRefreshing = false,
                    error = e.message ?: "Failed to load featured collection"
                )
            }
        }
    }

    private suspend fun loadCategories() {
        try {
            categoryUseCases.getCategories().collect { categories ->
                Log.d("ExploreViewModel", "Loaded categories: $categories")
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false,
                        categories = categories
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("ExploreViewModel", "Error loading categories", e)
            _uiState.update {
                it.copy(
                    error = e.message ?: "An unexpected error occurred loading categories",
                    isLoading = false,
                    isRefreshing = false
                )
            }
        }
    }

    private suspend fun loadPopularArtifacts() {
        try {
            // Load popular artifacts with specific queries
            val popularQueries = listOf(
                "bronze statue hellenistic",
                "roman amphora",
                "egyptian gold mask",
                "greek marble relief"
            )

            val popularArtifacts = mutableListOf<MuseumArtifact>()

            for (query in popularQueries) {
                val artifacts = museumUseCases.getMuseumArtifacts(query)
                if (artifacts.isNotEmpty()) {
                    // Take just the first result for each query
                    popularArtifacts.add(artifacts.first())
                }

                // If we have enough popular artifacts, break
                if (popularArtifacts.size >= 4) {
                    break
                }
            }

            _uiState.update {
                it.copy(
                    isLoading = false,
                    isRefreshing = false,
                    popularArtifacts = popularArtifacts
                )
            }
        } catch (e: Exception) {
            Log.e("ExploreViewModel", "Error loading popular artifacts", e)
            _uiState.update {
                it.copy(
                    error = e.message ?: "Failed to load popular artifacts",
                    isLoading = false,
                    isRefreshing = false
                )
            }
        }
    }

    fun searchByCategory(category: String) {
        viewModelScope.launch {
            try {
                val categoryArtifacts = museumUseCases.getMuseumArtifacts(category)
                // Do something with the category results
                // This could be navigating to another screen or updating state
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = e.message)
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}

data class ExploreUiState(
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false, // New state for pull-to-refresh
    val featuredArtifacts: List<MuseumArtifact> = emptyList(),
    val popularArtifacts: List<MuseumArtifact> = emptyList(),
    val error: String? = null
)
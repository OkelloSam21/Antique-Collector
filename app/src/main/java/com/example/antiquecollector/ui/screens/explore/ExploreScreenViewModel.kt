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
        loadFeaturedCollection()
        loadPopularArtifacts()
        loadCategories()
    }

    private fun loadFeaturedCollection() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

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
                        featuredArtifacts = featuredArtifacts
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }

    private fun loadCategories(){
        viewModelScope.launch {
            try {
                categoryUseCases.getCategories().collect { categories ->
                    Log.d("HomeViewModel", "Loaded categories: $categories")
                    _uiState.update {
                        it.copy(
                            isLoading = false, categories = categories
                        )
                    }
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
    }

    private fun loadPopularArtifacts() {
        viewModelScope.launch {
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
                    it.copy(popularArtifacts = popularArtifacts)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = e.message)
                }
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
    val categories : List<Category> = emptyList<Category>(),
    val isLoading: Boolean = false,
    val featuredArtifacts: List<MuseumArtifact> = emptyList(),
    val popularArtifacts: List<MuseumArtifact> = emptyList(),
    val error: String? = null
)
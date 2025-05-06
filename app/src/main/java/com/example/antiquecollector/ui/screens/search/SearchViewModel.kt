package com.example.antiquecollector.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antiquecollector.domain.model.Antique
import com.example.antiquecollector.domain.model.MuseumArtifact
import com.example.antiquecollector.domain.usecase.antique.AntiqueUseCases
import com.example.antiquecollector.domain.usecase.museum.MuseumUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchUiState(
    val searchQuery: String = "",
    val isSearching: Boolean = false,
    val localResults: List<Antique> = emptyList(),
    val remoteResults: List<MuseumArtifact> = emptyList(),
    val error: String? = null,
    val searchSourceType: SearchSourceType = SearchSourceType.BOTH
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val antiqueUseCases: AntiqueUseCases,
    private val museumUseCases: MuseumUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()
    
    private var searchJob: Job? = null

    fun setSearchSourceType(sourceType: SearchSourceType) {
        _uiState.update { it.copy(searchSourceType = sourceType) }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        
        // Cancel previous search if it's still running
        searchJob?.cancel()
        
        if (query.isEmpty()) {
            clearResults()
            return
        }
        
        // Add a small delay to avoid making too many search requests while typing
        searchJob = viewModelScope.launch {
            delay(300)
            performSearch(query)
        }
    }
    
    fun clearSearch() {
        _uiState.update { it.copy(searchQuery = "") }
        clearResults()
    }
    
    private fun clearResults() {
        _uiState.update { 
            it.copy(
                localResults = emptyList(),
                remoteResults = emptyList(),
                error = null
            )
        }
    }
    
    private fun performSearch(query: String) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isSearching = true, error = null) }
                
                when (uiState.value.searchSourceType) {
                    SearchSourceType.LOCAL -> {
                        searchLocal(query)
                    }
                    SearchSourceType.REMOTE -> {
                        searchRemote(query)
                    }
                    SearchSourceType.BOTH -> {
                        searchLocal(query)
                        searchRemote(query)
                    }
                }
                
                _uiState.update { it.copy(isSearching = false) }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isSearching = false,
                        error = "Error searching: ${e.message}"
                    )
                }
            }
        }
    }
    
    private suspend fun searchLocal(query: String) {
        try {
         antiqueUseCases.searchAntiquesUseCase(query).collect { antiquesList ->
             _uiState.update { it.copy(localResults = antiquesList) }
         }
        } catch (e: Exception) {
            _uiState.update { 
                it.copy(error = "Error searching local collection: ${e.message}")
            }
        }
    }
    
    private suspend fun searchRemote(query: String) {
        try {
            val results = museumUseCases.searchArtifacts(query)
            _uiState.update { it.copy(remoteResults = results) }
        } catch (e: Exception) {
            _uiState.update { 
                it.copy(error = "Error searching remote artifacts: ${e.message}")
            }
        }
    }
}
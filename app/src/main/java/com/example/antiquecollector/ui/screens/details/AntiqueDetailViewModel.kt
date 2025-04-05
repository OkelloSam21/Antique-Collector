package com.example.antiquecollector.ui.screens.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antiquecollector.domain.model.Antique
import com.example.antiquecollector.domain.usecase.antique.AntiqueUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AntiqueDetailViewModel @Inject constructor(
    private val antiqueUseCases: AntiqueUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(AntiqueDetailUiState())
    val uiState: StateFlow<AntiqueDetailUiState> = _uiState.asStateFlow()

    // Extract antique ID from saved state handle
    private val antiqueId: Long = checkNotNull(savedStateHandle["antiqueId"])

    init {
        loadAntique()
    }

    private fun loadAntique() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                antiqueUseCases.getAntiqueUseCase(antiqueId)?.let { antique ->
                    _uiState.update {
                        it.copy(
                            antique = antique,
                            isLoading = false
                        )
                    }
                } ?: run {
                    _uiState.update {
                        it.copy(
                            error = "Antique not found",
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = e.message ?: "An error occurred",
                        isLoading = false
                    )
                }
            }
        }
    }

    fun refreshAntique() {
        loadAntique()
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}

data class AntiqueDetailUiState(
    val antique: Antique? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
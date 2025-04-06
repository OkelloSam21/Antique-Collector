package com.example.antiquecollector.ui.screens.details

import androidx.lifecycle.SavedStateHandle

import androidx.lifecycle.ViewModel

import androidx.lifecycle.viewModelScope

import com.example.antiquecollector.domain.model.Antique
import com.example.antiquecollector.domain.model.MuseumArtifact

import com.example.antiquecollector.domain.usecase.antique.AntiqueUseCases
import com.example.antiquecollector.domain.usecase.museum.MuseumUseCases
import com.example.antiquecollector.util.ArtifactId

import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.flow.MutableStateFlow

import kotlinx.coroutines.flow.StateFlow

import kotlinx.coroutines.flow.asStateFlow

import kotlinx.coroutines.flow.update

import kotlinx.coroutines.launch

import javax.inject.Inject
import kotlin.text.get


@HiltViewModel

class AntiqueDetailViewModel @Inject constructor(

    private val antiqueUseCases: AntiqueUseCases,

    private val museumUseCases: MuseumUseCases,

    savedStateHandle: SavedStateHandle

) : ViewModel() {

    private val _uiState = MutableStateFlow(AntiqueDetailUiState())

    val uiState: StateFlow<AntiqueDetailUiState> = _uiState.asStateFlow()

    // Extract antique ID from saved state handle

//    private val artifactId: ArtifactId = checkNotNull(savedStateHandle["artifactId"])

//    init {
//        loadAntique()
//    }

    fun loadAntique(artifactId: ArtifactId) {

        viewModelScope.launch {

            try {

                _uiState.update { it.copy(isLoading = true) }

                when (artifactId) {
                    is ArtifactId.Local -> {
                        antiqueUseCases.getAntiqueUseCase(artifactId.id)?.let { antique ->
                            _uiState.update {
                                it.copy(
                                    antique = antique,
                                    isLoading = false
                                )
                            }
                        }
                    }

                    is ArtifactId.Remote -> {
                        museumUseCases.getArtifactByIdUseCase(artifactId.id)
                            ?.let { museumArtifact ->
                                _uiState.update {
                                    it.copy(
                                        museumArtifact = museumArtifact,
                                        isLoading = false
                                    )
                                }
                            }

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


//    fun refreshAntique() {
//
//        loadAntique()
//    }


    fun clearError() {

        _uiState.update { it.copy(error = null) }

    }

}


data class AntiqueDetailUiState(

    val antique: Antique? = null,

    val museumArtifact: MuseumArtifact? = null,

    val isLoading: Boolean = false,

    val error: String? = null

)
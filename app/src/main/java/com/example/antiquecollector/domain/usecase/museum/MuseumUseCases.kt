package com.example.antiquecollector.domain.usecase.museum

import javax.inject.Inject

/**
 * Combined class containing all museum-related use cases.
 * This makes it easier to inject all use cases where needed.
 */
data class MuseumUseCases @Inject constructor(
    val getMuseumArtifacts: GetMuseumArtifactsUseCase,
    val getSimilarMuseumArtifacts: GetSimilarMuseumArtifactsUseCase,
    val getArtifactByIdUseCase: GetArtifactByIdUseCase,
    val searchArtifacts: SearchArtifactsUseCase,
)
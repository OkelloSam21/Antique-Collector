package com.example.antiquecollector.domain.usecase.museum

import com.example.antiquecollector.domain.model.MuseumArtifact
import com.example.antiquecollector.domain.repository.MuseumRepository
import javax.inject.Inject

/**
 * Use case for searching museum artifacts.
 */
class GetMuseumArtifactsUseCase @Inject constructor(
    private val repository: MuseumRepository
) {
    /**
     * Search for museum artifacts matching the provided query.
     *
     * @param query The search query
     * @return List of museum artifacts matching the query
     */
    suspend operator fun invoke(query: String): List<MuseumArtifact> {
        if (query.isBlank()) {
            return emptyList()
        }
        
        return repository.searchArtifacts(query)
    }
}
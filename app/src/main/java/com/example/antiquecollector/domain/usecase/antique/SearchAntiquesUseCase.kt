package com.example.antiquecollector.domain.usecase.antique

import com.example.antiquecollector.domain.model.Antique
import com.example.antiquecollector.domain.repository.AntiqueRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for searching antiques by query.
 */
class SearchAntiquesUseCase @Inject constructor(
    private val repository: AntiqueRepository
) {
    /**
     * Search for antiques matching the provided query.
     *
     * @param query The search query
     * @return Flow of antiques matching the query
     */
    operator fun invoke(query: String): Flow<List<Antique>> {
        // Don't search if query is blank
        if (query.isBlank()) {
            return repository.getAllAntiques()
        }
        
        return repository.searchAntiques(query)
    }
}
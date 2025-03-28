package com.example.antiquecollector.domain.usecase.antique

import com.example.antiquecollector.domain.model.Antique
import com.example.antiquecollector.domain.repository.AntiqueRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving a specific antique by ID.
 */
class GetAntiquesUseCase @Inject constructor(
    private val repository: AntiqueRepository
) {
    /**
     * Get all the antiques.
     *
     * @return A list of all the antiques
     */
    suspend fun getAllAntiques(): Flow<List<Antique>> {
        return repository.getAllAntiques()
    }
}
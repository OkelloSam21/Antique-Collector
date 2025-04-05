package com.example.antiquecollector.domain.usecase.antique

import com.example.antiquecollector.domain.model.Antique
import com.example.antiquecollector.domain.repository.AntiqueRepository
import javax.inject.Inject

/**
 * Use case for retrieving a specific antique by ID.
 */
class GetAntiqueByIdUseCase @Inject constructor(
    private val repository: AntiqueRepository
) {
    /**
     * Get an antique by its ID.
     *
     * @param id The ID of the antique to retrieve
     * @return The antique if found, or null if not found
     */
    suspend operator fun invoke(id: Long): Antique? {
        return repository.getAntiqueById(id.toString())
    }

}
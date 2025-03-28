package com.example.antiquecollector.domain.usecase.antique

import com.example.antiquecollector.domain.model.Antique
import com.example.antiquecollector.domain.repository.AntiqueRepository
import javax.inject.Inject

/**
 * Use case for adding a new antique to the collection.
 */
class AddAntiqueUseCase @Inject constructor(
    private val repository: AntiqueRepository
) {
    /**
     * Add a new antique to the collection.
     *
     * @param antique The antique to add
     * @return The ID of the newly added antique
     */
    suspend operator fun invoke(antique: Antique): Long {
        return repository.addAntique(antique)
    }
}
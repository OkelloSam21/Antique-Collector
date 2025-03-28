package com.example.antiquecollector.domain.usecase.antique

import com.example.antiquecollector.domain.model.Antique
import com.example.antiquecollector.domain.repository.AntiqueRepository
import javax.inject.Inject

/**
 * Use case for deleting an antique from the collection.
 */
class DeleteAntiqueUseCase @Inject constructor(
    private val repository: AntiqueRepository
) {
    /**
     * Delete an antique from the collection.
     *
     * @param antique The antique to delete
     */
    suspend operator fun invoke(antique: Antique) {
        repository.deleteAntique(antique)
    }
}
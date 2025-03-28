package com.example.antiquecollector.domain.usecase.antique

import com.example.antiquecollector.domain.model.Antique
import com.example.antiquecollector.domain.repository.AntiqueRepository
import javax.inject.Inject

/**
 * Use case for updating an existing antique.
 */
class UpdateAntiqueUseCase @Inject constructor(
    private val repository: AntiqueRepository
) {
    /**
     * Update an existing antique.
     *
     * @param antique The updated antique details
     */
    suspend operator fun invoke(antique: Antique) {
        repository.updateAntique(antique)
    }
}
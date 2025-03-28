package com.example.antiquecollector.domain.usecase.antique

import com.example.antiquecollector.domain.model.Antique
import com.example.antiquecollector.domain.repository.AntiqueRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving antiques by category.
 */
class GetAntiquesByCategoryUseCase @Inject constructor(
    private val repository: AntiqueRepository
) {
    /**
     * Get antiques belonging to a specific category.
     *
     * @param categoryId The ID of the category to filter by
     * @return Flow of antiques in the specified category
     */
    operator fun invoke(categoryId: Long): Flow<List<Antique>> {
        return repository.getAntiquesByCategory(categoryId)
    }
}
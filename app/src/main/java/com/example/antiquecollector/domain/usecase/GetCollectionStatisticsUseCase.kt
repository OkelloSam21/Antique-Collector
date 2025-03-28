package com.example.antiquecollector.domain.usecase

import com.example.antiquecollector.domain.model.CollectionStatistics
import com.example.antiquecollector.domain.repository.AntiqueRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving collection statistics.
 */
class GetCollectionStatisticsUseCase @Inject constructor(
    private val repository: AntiqueRepository
) {
    /**
     * Get statistics about the collection.
     *
     * @return Flow of collection statistics
     */
    operator fun invoke(): Flow<CollectionStatistics> {
        return repository.getCollectionStatistics()
    }
}
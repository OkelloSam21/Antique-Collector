package com.example.antiquecollector.domain.usecase.category

import com.example.antiquecollector.domain.model.Category
import com.example.antiquecollector.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving all categories.
 */
class GetCategoriesUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    /**
     * Get all categories as a Flow.
     *
     * @return Flow of all categories
     */
    operator fun invoke(): Flow<List<Category>> {
        return repository.getAllCategories()
    }
}
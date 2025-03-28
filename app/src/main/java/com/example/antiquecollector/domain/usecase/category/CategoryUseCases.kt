package com.example.antiquecollector.domain.usecase.category

import javax.inject.Inject

/**
 * Combined class containing all category-related use cases.
 * This makes it easier to inject all use cases where needed.
 */
data class CategoryUseCases @Inject constructor(
    val getCategories: GetCategoriesUseCase
)
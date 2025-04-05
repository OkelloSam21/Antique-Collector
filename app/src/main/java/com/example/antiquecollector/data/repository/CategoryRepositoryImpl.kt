package com.example.antiquecollector.data.repository

import com.example.antiquecollector.data.local.dao.CategoryDao
import com.example.antiquecollector.data.local.entity.CategoryEntity
import com.example.antiquecollector.domain.model.Category
import com.example.antiquecollector.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of the CategoryRepository interface.
 */
class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository {
    
    override suspend fun getCategoryById(id: String): Category? {
        val categoryEntity = categoryDao.getCategoryById(id.toString()) ?: return null
        
        return Category(
            id = categoryEntity.id.toString(),
            name = categoryEntity.name,
            iconName = categoryEntity.iconName,
            description = categoryEntity.description
        )
    }
    
    override fun getAllCategories(): Flow<List<Category>> {
        return categoryDao.getAllCategories().map { categoryEntities ->
            categoryEntities.map { categoryEntity ->
                Category(
                    id = categoryEntity.id.toString(),
                    name = categoryEntity.name,
                    iconName = categoryEntity.iconName,
                    description = categoryEntity.description
                )
            }
        }
    }
    
    override suspend fun addCategory(category: Category): Long {
        val categoryEntity = CategoryEntity(
            name = category.name,
            iconName = category.iconName,
            description = category.description
        )
        return categoryDao.insertCategory(categoryEntity)
    }
    
    override suspend fun updateCategory(category: Category) {
        val categoryEntity = CategoryEntity(
            id = category.id.toLong(),
            name = category.name,
            iconName = category.iconName,
            description = category.description
        )
        categoryDao.updateCategory(categoryEntity)
    }
    
    override suspend fun deleteCategory(category: Category) {
        val categoryEntity = CategoryEntity(
            id = category.id.toLong(),
            name = category.name,
            iconName = category.iconName,
            description = category.description
        )
        categoryDao.deleteCategory(categoryEntity)
    }
}
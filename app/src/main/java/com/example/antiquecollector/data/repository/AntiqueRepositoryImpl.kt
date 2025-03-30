package com.example.antiquecollector.data.repository

import com.example.antiquecollector.data.local.dao.AntiqueDao
import com.example.antiquecollector.data.local.dao.CategoryDao
import com.example.antiquecollector.data.local.entity.AntiqueEntity
import com.example.antiquecollector.domain.model.Antique
import com.example.antiquecollector.domain.model.Category
import com.example.antiquecollector.domain.model.CollectionStatistics
import com.example.antiquecollector.domain.model.Condition
import com.example.antiquecollector.domain.repository.AntiqueRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

/**
 * Implementation of the AntiqueRepository interface.
 */
class AntiqueRepositoryImpl @Inject constructor(
    private val antiqueDao: AntiqueDao,
    private val categoryDao: CategoryDao
) : AntiqueRepository {
    
    override suspend fun getAntiqueById(id: Long): Antique? {
        val antiqueWithCategory = antiqueDao.getAntiqueWithCategory(id) ?: return null
        
        return Antique(
            id = antiqueWithCategory.antique.id,
            name = antiqueWithCategory.antique.name,
            category = antiqueWithCategory.category?.let {
                Category(
                    id = it.id,
                    name = it.name,
                    iconName = it.iconName,
                    description = it.description
                )
            },
            acquisitionDate = antiqueWithCategory.antique.acquisitionDate,
            currentValue = antiqueWithCategory.antique.currentValue,
            condition = Condition.fromStars(antiqueWithCategory.antique.condition),
            description = antiqueWithCategory.antique.description,
            location = antiqueWithCategory.antique.location,
            images = antiqueWithCategory.antique.imageUris ?: emptyList(),
            notes = antiqueWithCategory.antique.notes,
            materials = antiqueWithCategory.antique.materials,
            dimensions = antiqueWithCategory.antique.dimensions,
            origin = antiqueWithCategory.antique.origin,
            period = antiqueWithCategory.antique.period,
            lastModified = antiqueWithCategory.antique.lastModified
        )
    }
    
    override fun getAllAntiques(): Flow<List<Antique>> {
        return antiqueDao.getAllAntiquesWithCategories().map { antiqueWithCategoryList ->
            antiqueWithCategoryList.map { antiqueWithCategory ->
                Antique(
                    id = antiqueWithCategory.antique.id,
                    name = antiqueWithCategory.antique.name,
                    category = antiqueWithCategory.category?.let {
                        Category(
                            id = it.id,
                            name = it.name,
                            iconName = it.iconName,
                            description = it.description
                        )
                    },
                    acquisitionDate = antiqueWithCategory.antique.acquisitionDate,
                    currentValue = antiqueWithCategory.antique.currentValue,
                    condition = Condition.fromStars(antiqueWithCategory.antique.condition),
                    description = antiqueWithCategory.antique.description,
                    location = antiqueWithCategory.antique.location,
                    images = antiqueWithCategory.antique.imageUris ?: emptyList(),
                    notes = antiqueWithCategory.antique.notes,
                    materials = antiqueWithCategory.antique.materials,
                    dimensions = antiqueWithCategory.antique.dimensions,
                    origin = antiqueWithCategory.antique.origin,
                    period = antiqueWithCategory.antique.period,
                    lastModified = antiqueWithCategory.antique.lastModified
                )
            }
        }
    }
    
    override fun getRecentAntiques(limit: Int): Flow<List<Antique>> {
        return antiqueDao.getRecentAntiques(limit).map { antiqueEntities ->
            antiqueEntities.map { antiqueEntity ->
                // For recent antiques, we just need basic com.example.antiquecollector.ui.theme.helper.getInfo without categories
                // This is more efficient than fetching the full relations
                Antique(
                    id = antiqueEntity.id,
                    name = antiqueEntity.name,
                    acquisitionDate = antiqueEntity.acquisitionDate,
                    currentValue = antiqueEntity.currentValue,
                    condition = Condition.fromStars(antiqueEntity.condition),
                    images = antiqueEntity.imageUris ?: emptyList(),
                    lastModified = antiqueEntity.lastModified
                )
            }
        }
    }
    
    override fun getAntiquesByCategory(categoryId: Long): Flow<List<Antique>> {
        return antiqueDao.getAntiquesByCategory(categoryId).map { antiqueEntities ->
            antiqueEntities.map { antiqueEntity ->
                Antique(
                    id = antiqueEntity.id,
                    name = antiqueEntity.name,
                    acquisitionDate = antiqueEntity.acquisitionDate,
                    currentValue = antiqueEntity.currentValue,
                    condition = Condition.fromStars(antiqueEntity.condition),
                    description = antiqueEntity.description,
                    location = antiqueEntity.location,
                    images = antiqueEntity.imageUris ?: emptyList(),
                    notes = antiqueEntity.notes,
                    materials = antiqueEntity.materials,
                    dimensions = antiqueEntity.dimensions,
                    origin = antiqueEntity.origin,
                    period = antiqueEntity.period,
                    lastModified = antiqueEntity.lastModified
                )
            }
        }
    }
    
    override fun getCollectionStatistics(): Flow<CollectionStatistics> {
        val itemCount = antiqueDao.getAntiqueCount()
        val totalValue = antiqueDao.getTotalCollectionValue()
        val categoryCounts = antiqueDao.getCategoryCounts()
        val recentAdditions = getRecentAntiques(5)
        
        return combine(
            itemCount,
            totalValue,
            categoryCounts,
            recentAdditions
        ) { count, value, categoryCountMap, recent ->
            // Fetch all categories once - this is more efficient than fetching repeatedly
            val allCategories = categoryDao.getAllCategories().first()
            
            // Map category counts to domain models
            val categoryDomainCounts = categoryCountMap.mapNotNull { (categoryId, itemCount) ->
                categoryId?.let {
                    val categoryEntity = allCategories.find { it.id == categoryId }
                    categoryEntity?.let {
                        Category(
                            id = it.id,
                            name = it.name,
                            iconName = it.iconName,
                            description = it.description,
                            itemCount = itemCount
                        ) to itemCount
                    }
                }
            }.toMap()
            
            CollectionStatistics(
                totalItems = count,
                totalValue = value ?: 0.0,
                categoryCounts = categoryDomainCounts,
                recentAdditions = recent
            )
        }
    }
    
    override suspend fun addAntique(antique: Antique): Long {
        val antiqueEntity = AntiqueEntity(
            name = antique.name,
            categoryId = antique.category?.id,
            acquisitionDate = antique.acquisitionDate,
            currentValue = antique.currentValue,
            condition = antique.condition.stars,
            description = antique.description,
            location = antique.location,
            imageUris = antique.images.takeIf { it.isNotEmpty() },
            notes = antique.notes,
            materials = antique.materials,
            dimensions = antique.dimensions,
            origin = antique.origin,
            period = antique.period,
            lastModified = Date()
        )
        return antiqueDao.insertAntique(antiqueEntity)
    }
    
    override suspend fun updateAntique(antique: Antique) {
        val antiqueEntity = AntiqueEntity(
            id = antique.id,
            name = antique.name,
            categoryId = antique.category?.id,
            acquisitionDate = antique.acquisitionDate,
            currentValue = antique.currentValue,
            condition = antique.condition.stars,
            description = antique.description,
            location = antique.location,
            imageUris = antique.images.takeIf { it.isNotEmpty() },
            notes = antique.notes,
            materials = antique.materials,
            dimensions = antique.dimensions,
            origin = antique.origin,
            period = antique.period,
            lastModified = Date()
        )
        antiqueDao.updateAntique(antiqueEntity)
    }
    
    override suspend fun deleteAntique(antique: Antique) {
        val antiqueEntity = AntiqueEntity(
            id = antique.id,
            name = antique.name,
            categoryId = antique.category?.id,
            acquisitionDate = antique.acquisitionDate,
            currentValue = antique.currentValue,
            condition = antique.condition.stars,
            description = antique.description,
            location = antique.location,
            imageUris = antique.images.takeIf { it.isNotEmpty() },
            notes = antique.notes,
            materials = antique.materials,
            dimensions = antique.dimensions,
            origin = antique.origin,
            period = antique.period,
            lastModified = Date()
        )
        antiqueDao.deleteAntique(antiqueEntity)
    }
    
    override fun searchAntiques(query: String): Flow<List<Antique>> {
        return antiqueDao.searchAntiques(query).map { antiqueEntities ->
            antiqueEntities.map { antiqueEntity ->
                Antique(
                    id = antiqueEntity.id,
                    name = antiqueEntity.name,
                    acquisitionDate = antiqueEntity.acquisitionDate,
                    currentValue = antiqueEntity.currentValue,
                    condition = Condition.fromStars(antiqueEntity.condition),
                    description = antiqueEntity.description,
                    location = antiqueEntity.location,
                    images = antiqueEntity.imageUris ?: emptyList(),
                    notes = antiqueEntity.notes,
                    materials = antiqueEntity.materials,
                    dimensions = antiqueEntity.dimensions,
                    origin = antiqueEntity.origin,
                    period = antiqueEntity.period,
                    lastModified = antiqueEntity.lastModified
                )
            }
        }
    }
}
package com.example.antiquecollector.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.antiquecollector.data.local.entity.AntiqueEntity
import com.example.antiquecollector.data.local.entity.CategoryEntity

/**
 * Represents a relationship between an Antique and its Category.
 * This is used for queries that need to return both an antique and its associated category.
 */
data class AntiqueWithCategory(
    @Embedded val antique: AntiqueEntity,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: CategoryEntity?
)
package com.example.antiquecollector.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

/**
 * Room database entity representing an antique item in the collection.
 */
@Entity(
    tableName = "antiques",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("categoryId")]
)
data class AntiqueEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val categoryId: String?,
    val acquisitionDate: Date,
    val currentValue: Double,
    val condition: Int, // 1-5 rating
    val description: String?,
    val location: String?,
    val imageUris: List<String>?, // Stored as comma-separated list and converted using TypeConverter
    val notes: String?,
    val materials: String?,
    val dimensions: String?,
    val origin: String?,
    val period: String?,
    val lastModified: Date = Date()
)
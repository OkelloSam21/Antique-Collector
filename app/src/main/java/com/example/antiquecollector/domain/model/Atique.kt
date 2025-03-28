package com.example.antiquecollector.domain.model

import java.util.Date

/**
 * Domain model representing an antique item in the collection.
 */
data class Antique(
    val id: Long = 0,
    val name: String,
    val category: Category? = null,
    val acquisitionDate: Date,
    val currentValue: Double,
    val condition: Condition,
    val description: String? = null,
    val location: String? = null,
    val images: List<String> = emptyList(),
    val notes: String? = null,
    val materials: String? = null,
    val dimensions: String? = null,
    val origin: String? = null,
    val period: String? = null,
    val lastModified: Date = Date()
)


/**
 * Enum representing the condition of an antique item.
 */
enum class Condition(val stars: Int, val description: String) {
    POOR(1, "Poor condition with significant damage or deterioration."),
    FAIR(2, "Fair condition with noticeable wear and some damage."),
    GOOD(3, "Good condition with minor wear consistent with age."),
    VERY_GOOD(4, "Very good condition with minimal wear for its age."),
    EXCELLENT(5, "Excellent condition, exceptionally well preserved.");

    companion object {
        fun fromStars(stars: Int): Condition {
            return entries.find { it.stars == stars } ?: GOOD
        }
    }
}

/**
 * Domain model representing collection statistics.
 */
data class CollectionStatistics(
    val totalItems: Int = 0,
    val totalValue: Double = 0.0,
    val categoryCounts: Map<Category, Int> = emptyMap(),
    val recentAdditions: List<Antique> = emptyList()
)



/**
 * Domain model representing a user preference setting.
 */
data class UserPreference(
    val id: Long = 0,
    val key: String,
    val value: String,
    val lastModified: Date = Date()
)

/**
 * Enum representing the type of notification.
 */
enum class NotificationType {
    COLLECTION_REVIEW,
    MUSEUM_EVENT,
    SYSTEM
}
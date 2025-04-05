package com.example.antiquecollector.domain.model

/**
 * Domain model representing a category for antique items.
 */
data class Category(
    val id: String,
    val name: String,
    val iconName: String? = null,
    val description: String? = null,
    val itemCount: Int = 0
)

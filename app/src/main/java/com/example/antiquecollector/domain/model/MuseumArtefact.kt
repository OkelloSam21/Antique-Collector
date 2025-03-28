package com.example.antiquecollector.domain.model

/**
 * Domain model representing a museum artifact from an external API.
 */
data class MuseumArtifact(
    val id: String,
    val title: String,
    val artistName: String? = null,
    val objectDate: String? = null,
    val medium: String? = null,
    val dimensions: String? = null,
    val department: String? = null,
    val culture: String? = null,
    val period: String? = null,
    val dynasty: String? = null,
    val reign: String? = null,
    val publicDomain: Boolean = false,
    val primaryImageUrl: String? = null,
    val additionalImageUrls: List<String> = emptyList(),
    val objectUrl: String? = null
)
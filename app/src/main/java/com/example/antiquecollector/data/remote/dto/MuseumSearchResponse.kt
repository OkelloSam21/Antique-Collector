package com.example.antiquecollector.data.remote.dto

import com.example.antiquecollector.domain.model.MuseumArtifact
import com.google.gson.annotations.SerializedName

/**
 * DTO for the response from the museum search API.
 */
data class MuseumSearchResponse(
    @SerializedName("total") val total: Int,
    @SerializedName("objectIDs") val objectIds: List<Int>?
)

/**
 * DTO for artifact data from the Museum API.
 */
data class MuseumArtifactDto(
    @SerializedName("objectID") val objectId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("artistDisplayName") val artistName: String?,
    @SerializedName("objectDate") val objectDate: String?,
    @SerializedName("medium") val medium: String?,
    @SerializedName("dimensions") val dimensions: String?,
    @SerializedName("department") val department: String?,
    @SerializedName("culture") val culture: String?,
    @SerializedName("period") val period: String?,
    @SerializedName("dynasty") val dynasty: String?,
    @SerializedName("reign") val reign: String?,
    @SerializedName("isPublicDomain") val isPublicDomain: Boolean,
    @SerializedName("primaryImage") val primaryImage: String?,
    @SerializedName("additionalImages") val additionalImages: List<String>?,
    @SerializedName("objectURL") val objectURL: String?
)

/**
 * Extension function to convert MuseumArtifactDto to domain model MuseumArtifact
 */
fun MuseumArtifactDto.toDomainModel(): MuseumArtifact {
    return MuseumArtifact(
        id = objectId.toString(),
        title = title,
        artistName = artistName,
        objectDate = objectDate,
        medium = medium,
        dimensions = dimensions,
        department = department,
        culture = culture,
        period = period,
        dynasty = dynasty,
        reign = reign,
        publicDomain = isPublicDomain,
        primaryImageUrl = primaryImage,
        additionalImageUrls = additionalImages ?: emptyList(),
        objectUrl = objectURL
    )
}
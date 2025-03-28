package com.example.antiquecollector.data.remote.api

import com.example.antiquecollector.data.remote.dto.MuseumArtifactDto
import com.example.antiquecollector.data.remote.dto.MuseumSearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit service for the Metropolitan Museum of Art API.
 * Documentation: https://metmuseum.github.io/
 */
interface MuseumApiService {

    /**
     * Get a list of object IDs for a search query.
     * @param q The search query
     * @param hasImages Whether the objects should have images (true/false)
     * @param departmentId Filter by department ID
     */
    @GET("search")
    suspend fun searchObjects(
        @Query("q") q: String,
        @Query("hasImages") hasImages: Boolean = true,
        @Query("departmentId") departmentId: Int? = null
    ): MuseumSearchResponse

    /**
     * Get detailed information about a specific object.
     * @param objectId The ID of the object to get details for
     */
    @GET("objects/{objectId}")
    suspend fun getObjectDetails(
        @Path("objectId") objectId: Int
    ): MuseumArtifactDto

    /**
     * Get a list of all departments.
     */
    @GET("departments")
    suspend fun getDepartments(): Map<String, List<Map<String, Any>>>

    companion object {
        const val BASE_URL = "https://collectionapi.metmuseum.org/public/collection/v1/"
    }
}
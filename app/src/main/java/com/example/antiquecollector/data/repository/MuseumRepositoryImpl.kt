package com.example.antiquecollector.data.repository

import com.example.antiquecollector.data.remote.api.MuseumApiService
import com.example.antiquecollector.data.remote.dto.toDomainModel
import com.example.antiquecollector.data.remote.util.Resource
import com.example.antiquecollector.data.remote.util.safeApiCall
import com.example.antiquecollector.domain.model.MuseumArtifact
import com.example.antiquecollector.domain.repository.MuseumRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

/**
 * Implementation of the MuseumRepository interface.
 */
class MuseumRepositoryImpl @Inject constructor(
    private val museumApiService: MuseumApiService
) : MuseumRepository {
    
    override suspend fun searchArtifacts(query: String): List<MuseumArtifact> {
        val response = safeApiCall {
            museumApiService.searchObjects(query, hasImages = true)
        }
        
        return when (response) {
            is Resource.Success -> {
                val objectIds = response.data.objectIds?.take(15) ?: emptyList()
                fetchArtifactDetails(objectIds)
            }
            is Resource.Error -> emptyList()
            is Resource.Loading -> emptyList()
        }
    }
    
    override suspend fun getArtifactById(id: String): MuseumArtifact? {
        val response = safeApiCall {
            museumApiService.getObjectDetails(id.toInt())
        }
        
        return when (response) {
            is Resource.Success -> response.data.toDomainModel()
            is Resource.Error -> null
            is Resource.Loading -> null
        }
    }
    
    override suspend fun getSimilarArtifacts(keywords: List<String>, limit: Int): List<MuseumArtifact> {
        // Combine keywords into a search string
        val searchQuery = keywords.joinToString(" OR ")
        
        // If no keywords, return empty list
        if (searchQuery.isBlank()) {
            return emptyList()
        }
        
        // Search for artifacts matching the keywords
        return searchArtifacts(searchQuery).take(limit)
    }
    
    override suspend fun getArtifactsByDepartment(department: String, limit: Int): List<MuseumArtifact> {
        // The MET API doesn't directly support searching by department name
        // We'll search for the department name and then filter results
        return searchArtifacts(department).filter {
            it.department?.contains(department, ignoreCase = true) == true
        }.take(limit)
    }
    
    override suspend fun getArtifactsByCultureOrPeriod(
        culture: String?,
        period: String?,
        limit: Int
    ): List<MuseumArtifact> {
        // Build search query from culture and/or period
        val searchTerms = listOfNotNull(culture, period)
        if (searchTerms.isEmpty()) {
            return emptyList()
        }
        
        val searchQuery = searchTerms.joinToString(" OR ")
        
        // Search for artifacts and then filter by culture and period if provided
        return searchArtifacts(searchQuery).filter { artifact ->
            (culture == null || artifact.culture?.contains(culture, ignoreCase = true) == true) &&
                    (period == null || artifact.period?.contains(period, ignoreCase = true) == true)
        }.take(limit)
    }
    
    /**
     * Helper method to fetch details for multiple artifacts in parallel.
     */
    private suspend fun fetchArtifactDetails(objectIds: List<Int>): List<MuseumArtifact> = coroutineScope {
        val deferredArtifacts = objectIds.map { objectId ->
            async {
                val response = safeApiCall {
                    museumApiService.getObjectDetails(objectId)
                }
                
                when (response) {
                    is Resource.Success -> response.data.toDomainModel()
                    else -> null
                }
            }
        }
        
        deferredArtifacts.awaitAll().filterNotNull()
    }
}
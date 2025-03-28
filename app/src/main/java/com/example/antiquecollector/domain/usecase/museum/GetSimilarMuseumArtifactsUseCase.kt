package com.example.antiquecollector.domain.usecase.museum

import com.example.antiquecollector.domain.model.Antique
import com.example.antiquecollector.domain.model.MuseumArtifact
import com.example.antiquecollector.domain.repository.MuseumRepository
import javax.inject.Inject

/**
 * Use case for finding museum artifacts similar to a given antique.
 */
class GetSimilarMuseumArtifactsUseCase @Inject constructor(
    private val repository: MuseumRepository
) {
    /**
     * Find museum artifacts similar to the provided antique.
     *
     * @param antique The antique to find similar artifacts for
     * @param limit The maximum number of artifacts to return (default: 10)
     * @return List of similar museum artifacts
     */
    suspend operator fun invoke(antique: Antique, limit: Int = 10): List<MuseumArtifact> {
        // Extract relevant keywords from the antique
        val keywords = buildKeywords(antique)
        
        // If no keywords, return empty list
        if (keywords.isEmpty()) {
            return emptyList()
        }
        
        return repository.getSimilarArtifacts(keywords, limit)
    }
    
    /**
     * Build a list of keywords from an antique's properties.
     */
    private fun buildKeywords(antique: Antique): List<String> {
        val keywords = mutableListOf<String>()
        
        // Add category name if available
        antique.category?.name?.let { keywords.add(it) }
        
        // Add other relevant properties
        antique.materials?.split(",")?.map { it.trim() }?.let { keywords.addAll(it) }
        antique.origin?.let { keywords.add(it) }
        antique.period?.let { keywords.add(it) }
        
        // Add name parts that could be relevant (filter out common words)
        antique.name.split(" ")
            .filter { it.length > 3 }
            .filter { it.lowercase() !in commonWords }
            .let { keywords.addAll(it) }
        
        return keywords.distinct()
    }
    
    // Common words to exclude from keyword extraction
    private val commonWords = setOf(
        "the", "and", "for", "with", "from", "that", "this", "item", "antique", "old", "vintage"
    )
}
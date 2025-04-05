package com.example.antiquecollector.domain.repository

import com.example.antiquecollector.domain.model.Antique
import com.example.antiquecollector.domain.model.Category
import com.example.antiquecollector.domain.model.CollectionStatistics
import com.example.antiquecollector.domain.model.MuseumArtifact
import com.example.antiquecollector.domain.model.Notification
import com.example.antiquecollector.domain.model.UserPreference
import kotlinx.coroutines.flow.Flow
import java.util.Date

/**
 * Repository interface for antique-related operations.
 */
interface AntiqueRepository {
    /**
     * Get a single antique by ID.
     */
    suspend fun getAntiqueById(id: String): Antique?
    
    /**
     * Get all antiques as a Flow.
     */
    fun getAllAntiques(): Flow<List<Antique>>
    
    /**
     * Get recently added antiques.
     */
    fun getRecentAntiques(limit: Int): Flow<List<Antique>>
    
    /**
     * Get antiques by category.
     */
    fun getAntiquesByCategory(categoryId: String): Flow<List<Antique>>
    
    /**
     * Get collection statistics.
     */
    fun getCollectionStatistics(): Flow<CollectionStatistics>
    
    /**
     * Add a new antique to the collection.
     */
    suspend fun addAntique(antique: Antique): Long
    
    /**
     * Update an existing antique.
     */
    suspend fun updateAntique(antique: Antique)
    
    /**
     * Delete an antique from the collection.
     */
    suspend fun deleteAntique(antique: Antique)
    
    /**
     * Search antiques by query string.
     */
    fun searchAntiques(query: String): Flow<List<Antique>>
}

/**
 * Repository interface for category-related operations.
 */
interface CategoryRepository {
    /**
     * Get a single category by ID.
     */
    suspend fun getCategoryById(id: String): Category?
    
    /**
     * Get all categories as a Flow.
     */
    fun getAllCategories(): Flow<List<Category>>
    
    /**
     * Add a new category.
     */
    suspend fun addCategory(category: Category): Long
    
    /**
     * Update an existing category.
     */
    suspend fun updateCategory(category: Category)
    
    /**
     * Delete a category.
     */
    suspend fun deleteCategory(category: Category)
}

/**
 * Repository interface for museum artifact operations.
 */
interface MuseumRepository {
    /**
     * Search museum artifacts.
     */
    suspend fun searchArtifacts(query: String): List<MuseumArtifact>
    
    /**
     * Get artifact details by ID.
     */
    suspend fun getArtifactById(id: String): MuseumArtifact?
    
    /**
     * Get similar artifacts based on provided keywords.
     */
    suspend fun getSimilarArtifacts(keywords: List<String>, limit: Int = 10): List<MuseumArtifact>
    
    /**
     * Get artifacts by department.
     */
    suspend fun getArtifactsByDepartment(department: String, limit: Int = 20): List<MuseumArtifact>
    
    /**
     * Get artifacts by culture or period.
     */
    suspend fun getArtifactsByCultureOrPeriod(culture: String?, period: String?, limit: Int = 20): List<MuseumArtifact>
}

/**
 * Repository interface for user preference operations.
 */
interface PreferenceRepository {
    /**
     * Get a preference by key.
     */
    suspend fun getPreference(key: String): UserPreference?
    
    /**
     * Get a preference value by key.
     */
    suspend fun getPreferenceValue(key: String, defaultValue: String = ""): String
    
    /**
     * Set a preference value.
     */
    suspend fun setPreference(key: String, value: String)
    
    /**
     * Get all preferences.
     */
    fun getAllPreferences(): Flow<List<UserPreference>>
    
    /**
     * Delete a preference.
     */
    suspend fun deletePreference(key: String)
    
    /**
     * Clear all preferences.
     */
    suspend fun clearAllPreferences()
}

/**
 * Repository interface for notification operations.
 */
interface NotificationRepository {
    /**
     * Get a notification by ID.
     */
    suspend fun getNotificationById(id: Long): Notification?
    
    /**
     * Get all notifications.
     */
    fun getAllNotifications(): Flow<List<Notification>>
    
    /**
     * Get unread notifications.
     */
    fun getUnreadNotifications(): Flow<List<Notification>>
    
    /**
     * Add a new notification.
     */
    suspend fun addNotification(notification: Notification): Long
    
    /**
     * Mark a notification as read.
     */
    suspend fun markNotificationAsRead(id: Long)
    
    /**
     * Mark all notifications as read.
     */
    suspend fun markAllNotificationsAsRead()
    
    /**
     * Delete a notification.
     */
    suspend fun deleteNotification(id: Long)
    
    /**
     * Delete all notifications.
     */
    suspend fun deleteAllNotifications()
}
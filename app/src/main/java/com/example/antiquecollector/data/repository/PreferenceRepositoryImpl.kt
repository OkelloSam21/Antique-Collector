package com.example.antiquecollector.data.repository

import com.example.antiquecollector.data.local.dao.PreferenceDao
import com.example.antiquecollector.data.local.entity.UserPreferenceEntity
import com.example.antiquecollector.domain.model.UserPreference
import com.example.antiquecollector.domain.repository.PreferenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

/**
 * Implementation of the PreferenceRepository interface.
 */
class PreferenceRepositoryImpl @Inject constructor(
    private val preferenceDao: PreferenceDao
) : PreferenceRepository {
    
    override suspend fun getPreference(key: String): UserPreference? {
        val entity = preferenceDao.getPreferenceByKey(key) ?: return null
        return UserPreference(
            id = entity.id,
            key = entity.key,
            value = entity.value,
            lastModified = entity.lastModified
        )
    }
    
    override suspend fun getPreferenceValue(key: String, defaultValue: String): String {
        return preferenceDao.getPreferenceValue(key) ?: defaultValue
    }
    
    override suspend fun setPreference(key: String, value: String) {
        val existing = preferenceDao.getPreferenceByKey(key)
        
        if (existing != null) {
            preferenceDao.updatePreferenceValue(key, value)
        } else {
            preferenceDao.insertPreference(
                UserPreferenceEntity(
                    key = key,
                    value = value,
                    lastModified = Date()
                )
            )
        }
    }
    
    override fun getAllPreferences(): Flow<List<UserPreference>> {
        return preferenceDao.getAllPreferences().map { entities ->
            entities.map { entity ->
                UserPreference(
                    id = entity.id,
                    key = entity.key,
                    value = entity.value,
                    lastModified = entity.lastModified
                )
            }
        }
    }
    
    override suspend fun deletePreference(key: String) {
        preferenceDao.deletePreference(key)
    }
    
    override suspend fun clearAllPreferences() {
        preferenceDao.deleteAllPreferences()
    }
}
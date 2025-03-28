package com.example.antiquecollector.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.antiquecollector.data.local.entity.UserPreferenceEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the user_preferences table.
 */
@Dao
interface PreferenceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPreference(preference: UserPreferenceEntity): Long

    @Query("SELECT * FROM user_preferences WHERE `key` = :key LIMIT 1")
    suspend fun getPreferenceByKey(key: String): UserPreferenceEntity?

    @Query("SELECT * FROM user_preferences ORDER BY `key` ASC")
    fun getAllPreferences(): Flow<List<UserPreferenceEntity>>

    @Query("DELETE FROM user_preferences WHERE `key` = :key")
    suspend fun deletePreference(key: String)

    @Query("DELETE FROM user_preferences")
    suspend fun deleteAllPreferences()
    
    @Query("UPDATE user_preferences SET value = :value, lastModified = :timestamp WHERE `key` = :key")
    suspend fun updatePreferenceValue(key: String, value: String, timestamp: Long = System.currentTimeMillis())
    
    @Query("SELECT value FROM user_preferences WHERE `key` = :key LIMIT 1")
    suspend fun getPreferenceValue(key: String): String?
}
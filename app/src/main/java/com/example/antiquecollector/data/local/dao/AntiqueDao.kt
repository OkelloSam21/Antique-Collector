package com.example.antiquecollector.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.antiquecollector.data.local.entity.AntiqueEntity
import com.example.antiquecollector.data.local.relation.AntiqueWithCategory
import kotlinx.coroutines.flow.Flow
import java.util.Date

/**
 * Data Access Object for the antiques table.
 */
@Dao
interface AntiqueDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAntique(antique: AntiqueEntity): Long

    @Update
    suspend fun updateAntique(antique: AntiqueEntity)

    @Delete
    suspend fun deleteAntique(antique: AntiqueEntity)

    @Query("SELECT * FROM antiques WHERE id = :id")
    suspend fun getAntiqueById(id: Long): AntiqueEntity?

    @Query("SELECT * FROM antiques ORDER BY lastModified DESC")
    fun getAllAntiques(): Flow<List<AntiqueEntity>>

    @Query("SELECT * FROM antiques ORDER BY lastModified DESC LIMIT :limit")
    fun getRecentAntiques(limit: Int): Flow<List<AntiqueEntity>>

    @Query("SELECT * FROM antiques WHERE categoryId = :categoryId ORDER BY name ASC")
    fun getAntiquesByCategory(categoryId: String): Flow<List<AntiqueEntity>>

    @Query("SELECT COUNT(*) FROM antiques")
    fun getAntiqueCount(): Flow<Int>

    @Query("SELECT SUM(currentValue) FROM antiques")
    fun getTotalCollectionValue(): Flow<Double?>

    @Query("SELECT categoryId, COUNT(*) as count FROM antiques GROUP BY categoryId")
    fun getCategoryCounts(): Flow<List<CategoryCount>>

    @Transaction
    @Query("SELECT * FROM antiques WHERE id = :id")
    suspend fun getAntiqueWithCategory(id: String): AntiqueWithCategory?

    @Transaction
    @Query("SELECT * FROM antiques ORDER BY lastModified DESC")
    fun getAllAntiquesWithCategories(): Flow<List<AntiqueWithCategory>>

    @Transaction
    @Query("SELECT * FROM antiques WHERE acquisitionDate > :date ORDER BY acquisitionDate DESC")
    fun getAntiquesAcquiredSince(date: Date): Flow<List<AntiqueWithCategory>>

    @Query("SELECT * FROM antiques WHERE " +
            "name LIKE '%' || :query || '%' OR " +
            "description LIKE '%' || :query || '%' OR " +
            "origin LIKE '%' || :query || '%' OR " +
            "period LIKE '%' || :query || '%'")
    fun searchAntiques(query: String): Flow<List<AntiqueEntity>>

    @Query("DELETE FROM antiques")
    suspend fun deleteAllAntiques()

    @Query("SELECT * FROM antiques WHERE currentValue > :minValue")
    fun getAntiquesByMinValue(minValue: Double): Flow<List<AntiqueEntity>>

    @Query("SELECT * FROM antiques WHERE condition >= :minCondition")
    fun getAntiquesByMinCondition(minCondition: Int): Flow<List<AntiqueEntity>>
}

data class CategoryCount(
    val categoryId: Long?, // Use Long? to handle potential null category IDs
    val count: Int
)
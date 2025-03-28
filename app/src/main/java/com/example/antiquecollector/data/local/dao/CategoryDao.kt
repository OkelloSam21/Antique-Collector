package com.example.antiquecollector.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.antiquecollector.data.local.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the categories table.
 */
@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity): Long

    @Update
    suspend fun updateCategory(category: CategoryEntity)

    @Delete
    suspend fun deleteCategory(category: CategoryEntity)

    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getCategoryById(id: Long): CategoryEntity?

    @Query("SELECT * FROM categories ORDER BY name ASC")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    @Query("SELECT COUNT(*) FROM categories")
    fun getCategoryCount(): Flow<Int>
    
    @Query("SELECT * FROM categories WHERE name LIKE '%' || :query || '%'")
    fun searchCategories(query: String): Flow<List<CategoryEntity>>
    
    @Query("DELETE FROM categories")
    suspend fun deleteAllCategories()
    
    @Query("SELECT c.* FROM categories c " +
            "JOIN antiques a ON c.id = a.categoryId " +
            "GROUP BY c.id " +
            "ORDER BY COUNT(a.id) DESC")
    fun getCategoriesByPopularity(): Flow<List<CategoryEntity>>
}
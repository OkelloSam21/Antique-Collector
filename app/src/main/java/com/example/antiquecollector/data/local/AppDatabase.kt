package com.example.antiquecollector.data.local

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.antiquecollector.data.local.dao.AntiqueDao
import com.example.antiquecollector.data.local.dao.CategoryDao
import com.example.antiquecollector.data.local.dao.NotificationDao
import com.example.antiquecollector.data.local.dao.PreferenceDao
import com.example.antiquecollector.data.local.entity.AntiqueEntity
import com.example.antiquecollector.data.local.entity.CategoryEntity
import com.example.antiquecollector.data.local.entity.NotificationEntity
import com.example.antiquecollector.data.local.entity.UserPreferenceEntity
import com.example.antiquecollector.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider
import kotlin.math.log

/**
 * The Room database for the Antique Collector application.
 */
@Database(
    entities = [
        AntiqueEntity::class,
        CategoryEntity::class,
        UserPreferenceEntity::class,
        NotificationEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun antiqueDao(): AntiqueDao
    abstract fun categoryDao(): CategoryDao
    abstract fun preferenceDao(): PreferenceDao
    abstract fun notificationDao(): NotificationDao

    /**
     * Callback to populate the database with initial data.
     */
    class Callback @Inject constructor(
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {

        private lateinit var database: AppDatabase

        fun setDatabase(db: AppDatabase) {
            database = db
        }
        
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            Log.d("AppDatabase", "Database created, populating with default data")
            // Populate the database in the background
            applicationScope.launch {
                populateDatabase()
                Log.d("AppDatabase", "db populated successfully")
            }
        }
        
        private suspend fun populateDatabase() {
            // Add default categories
            if (!::database.isInitialized) {
                Log.d("AppDatabase", "Database initialized check")
                return
            }
            val categoryDao = database.categoryDao()
            
            val defaultCategories = listOf(
                CategoryEntity(name = "Furniture", iconName = "furniture", description = "Tables, chairs, cabinets, and other furniture items"),
                CategoryEntity(name = "Art", iconName = "art", description = "Paintings, drawings, sculptures and other art pieces"),
                CategoryEntity(name = "Jewelry", iconName = "jewelry", description = "Rings, necklaces, bracelets and other jewelry items"),
                CategoryEntity(name = "Watches", iconName = "watch", description = "Pocket watches, wristwatches, and clocks"),
                CategoryEntity(name = "Books", iconName = "book", description = "Rare and antique books, manuscripts"),
                CategoryEntity(name = "Ceramics", iconName = "ceramics", description = "Pottery, porcelain, and other ceramic items"),
                CategoryEntity(name = "Glassware", iconName = "glassware", description = "Antique glass items"),
                CategoryEntity(name = "Textiles", iconName = "textiles", description = "Rugs, tapestries, and other textile items"),
                CategoryEntity(name = "Timepieces", iconName = "clock", description = "Clocks and timing devices"),
                CategoryEntity(name = "Sculptures", iconName = "sculpture", description = "Three-dimensional artworks"),
                CategoryEntity(name = "Paintings", iconName = "painting", description = "Oil, watercolor, and other paintings"),
                CategoryEntity(name = "Accessories", iconName = "accessories", description = "Vintage and antique personal accessories")
            )
            
            defaultCategories.forEach { category ->
                categoryDao.insertCategory(category)
            }
            
            // Add default preferences
            val preferenceDao = database.preferenceDao()
            
            val defaultPreferences = listOf(
                UserPreferenceEntity(key = "currency", value = "USD"),
                UserPreferenceEntity(key = "theme", value = "light"),
                UserPreferenceEntity(key = "notification_collection_review", value = "true"),
                UserPreferenceEntity(key = "notification_museum_events", value = "true"),
                UserPreferenceEntity(key = "review_period", value = "30"), // Days
                UserPreferenceEntity(key = "default_location", value = "Home")
            )
            
            defaultPreferences.forEach { preference ->
                preferenceDao.insertPreference(preference)
            }

        }
    }
//
//    companion object {
//        @Volatile
//        private var INSTANCE: AppDatabase? = null
//
//        fun getInstance(context: Context): AppDatabase =
//            INSTANCE ?: synchronized(this) {
//                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
//            }
//
//        private fun buildDatabase(context: Context) =
//            Room.databaseBuilder(
//                context.applicationContext,
//                AppDatabase::class.java,
//                "antique_collector.db"
//            )
//                .fallbackToDestructiveMigration()
//                .build()
//    }
}
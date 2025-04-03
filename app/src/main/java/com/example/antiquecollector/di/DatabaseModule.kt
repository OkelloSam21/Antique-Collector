package com.example.antiquecollector.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.antiquecollector.data.local.AppDatabase
import com.example.antiquecollector.data.local.dao.AntiqueDao
import com.example.antiquecollector.data.local.dao.CategoryDao
import com.example.antiquecollector.data.local.dao.PreferenceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideCallBack(@ApplicationScope scope: CoroutineScope): AppDatabase.Callback{
        return AppDatabase.Callback(scope)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        callback: AppDatabase.Callback
    ): AppDatabase {
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "antique_collector_database"
        )
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()

        callback.setDatabase(db)

        return db
    }

    @Provides
    fun provideAntiqueDao(appDatabase: AppDatabase): AntiqueDao {
        return appDatabase.antiqueDao()
    }

    @Provides
    fun provideCategoryDao(appDatabase: AppDatabase): CategoryDao {
        return appDatabase.categoryDao()
    }

    @Provides
    @Singleton
    fun providePreferenceDao(appDatabase: AppDatabase): PreferenceDao {
        return appDatabase.preferenceDao()
    }


}
package com.example.antiquecollector.di

import android.content.Context
import androidx.room.Room
import com.example.antiquecollector.data.local.AppDatabase
import com.example.antiquecollector.data.local.dao.AntiqueDao
import com.example.antiquecollector.data.local.dao.CategoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "antique_collector_database"
        ).build()
    }

    @Provides
    fun provideAntiqueDao(appDatabase: AppDatabase): AntiqueDao {
        return appDatabase.antiqueDao()
    }

    @Provides
    fun provideCategoryDao(appDatabase: AppDatabase): CategoryDao {
        return appDatabase.categoryDao()
    }

}
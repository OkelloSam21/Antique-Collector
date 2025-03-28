package com.example.antiquecollector.di

import com.example.antiquecollector.data.local.dao.AntiqueDao
import com.example.antiquecollector.data.local.dao.CategoryDao
import com.example.antiquecollector.data.local.dao.NotificationDao
import com.example.antiquecollector.data.local.dao.PreferenceDao
import com.example.antiquecollector.data.remote.api.MuseumApiService
import com.example.antiquecollector.data.repository.AntiqueRepositoryImpl
import com.example.antiquecollector.data.repository.CategoryRepositoryImpl
import com.example.antiquecollector.data.repository.MuseumRepositoryImpl
import com.example.antiquecollector.data.repository.NotificationRepositoryImpl
import com.example.antiquecollector.data.repository.PreferenceRepositoryImpl
import com.example.antiquecollector.domain.repository.AntiqueRepository
import com.example.antiquecollector.domain.repository.CategoryRepository
import com.example.antiquecollector.domain.repository.MuseumRepository
import com.example.antiquecollector.domain.repository.NotificationRepository
import com.example.antiquecollector.domain.repository.PreferenceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideAntiqueRepository(
        antiqueDao: AntiqueDao,
        categoryDao: CategoryDao
    ): AntiqueRepository {
        return AntiqueRepositoryImpl(antiqueDao, categoryDao)
    }

    @Singleton
    @Provides
    fun provideCategoryRepository(
        categoryDao: CategoryDao
    ): CategoryRepository {
        return CategoryRepositoryImpl(categoryDao)
    }

    @Singleton
    @Provides
    fun provideMuseumRepository(
        museumApiService: MuseumApiService
    ): MuseumRepository {
        return MuseumRepositoryImpl(museumApiService)
    }

    @Singleton
    @Provides
    fun providePreferenceRepository(
        preferenceDao: PreferenceDao
    ): PreferenceRepository {
        return PreferenceRepositoryImpl(preferenceDao)
    }

    @Singleton
    @Provides
    fun provideNotificationRepository(
        notificationDao: NotificationDao
    ): NotificationRepository {
        return NotificationRepositoryImpl(notificationDao)
    }
}
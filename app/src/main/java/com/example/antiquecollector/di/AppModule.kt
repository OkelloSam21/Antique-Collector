package com.example.antiquecollector.di

import android.content.Context
import com.example.antiquecollector.util.CurrencyFormatter
import com.example.antiquecollector.util.DateUtils
import com.example.antiquecollector.util.ImageUtils
import com.example.antiquecollector.util.NotificationUtils
import com.example.antiquecollector.util.ValidationUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Annotation for application scope coroutine.
 */
@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope

/**
 * Main application module for Dagger Hilt.
 * Provides application-level dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    /**
     * Provides the application-scoped CoroutineScope.
     */
    @ApplicationScope
    @Singleton
    @Provides
    fun provideApplicationScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }
    
    /**
     * Provides the DateUtils instance.
     */
    @Singleton
    @Provides
    fun provideDateUtils(): DateUtils {
        return DateUtils()
    }
    
    /**
     * Provides the CurrencyFormatter instance.
     */
    @Singleton
    @Provides
    fun provideCurrencyFormatter(): CurrencyFormatter {
        return CurrencyFormatter()
    }
    
    /**
     * Provides the ValidationUtils instance.
     */
    @Singleton
    @Provides
    fun provideValidationUtils(): ValidationUtils {
        return ValidationUtils()
    }
    
    /**
     * Provides the ImageUtils instance.
     */
    @Singleton
    @Provides
    fun provideImageUtils(@ApplicationContext context: Context): ImageUtils {
        return ImageUtils(context)
    }
    
    /**
     * Provides the NotificationUtils instance.
     */
    @Singleton
    @Provides
    fun provideNotificationUtils(@ApplicationContext context: Context): NotificationUtils {
        return NotificationUtils(context)
    }
}
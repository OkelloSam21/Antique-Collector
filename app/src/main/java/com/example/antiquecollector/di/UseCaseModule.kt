package com.example.antiquecollector.di

import com.example.antiquecollector.domain.repository.AntiqueRepository
import com.example.antiquecollector.domain.repository.CategoryRepository
import com.example.antiquecollector.domain.repository.MuseumRepository
import com.example.antiquecollector.domain.usecase.antique.AddAntiqueUseCase
import com.example.antiquecollector.domain.usecase.category.CategoryUseCases
import com.example.antiquecollector.domain.usecase.antique.GetAntiqueByIdUseCase
import com.example.antiquecollector.domain.usecase.category.GetCategoriesUseCase
import com.example.antiquecollector.domain.usecase.GetCollectionStatisticsUseCase
import com.example.antiquecollector.domain.usecase.antique.AntiqueUseCases
import com.example.antiquecollector.domain.usecase.museum.GetMuseumArtifactsUseCase
import com.example.antiquecollector.domain.usecase.museum.GetSimilarMuseumArtifactsUseCase
import com.example.antiquecollector.domain.usecase.antique.DeleteAntiqueUseCase
import com.example.antiquecollector.domain.usecase.antique.GetAntiquesByCategoryUseCase
import com.example.antiquecollector.domain.usecase.antique.GetAntiquesUseCase
import com.example.antiquecollector.domain.usecase.museum.MuseumUseCases
import com.example.antiquecollector.domain.usecase.antique.SearchAntiquesUseCase
import com.example.antiquecollector.domain.usecase.antique.UpdateAntiqueUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Module for providing use cases.
 */
@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    // Antique use cases

    @Singleton
    @Provides
    fun provideGetAntiqueUseCase(repository: AntiqueRepository): GetAntiqueByIdUseCase {
        return GetAntiqueByIdUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetAntiquesUseCase(repository: AntiqueRepository): GetAntiquesUseCase {
        return GetAntiquesUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideAddAntiqueUseCase(repository: AntiqueRepository): AddAntiqueUseCase {
        return AddAntiqueUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideUpdateAntiqueUseCase(repository: AntiqueRepository): UpdateAntiqueUseCase {
        return UpdateAntiqueUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideDeleteAntiqueUseCase(repository: AntiqueRepository): DeleteAntiqueUseCase {
        return DeleteAntiqueUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideSearchAntiquesUseCase(repository: AntiqueRepository): SearchAntiquesUseCase {
        return SearchAntiquesUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetAntiquesByCategoryUseCase(repository: AntiqueRepository): GetAntiquesByCategoryUseCase {
        return GetAntiquesByCategoryUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetCollectionStatisticsUseCase(repository: AntiqueRepository): GetCollectionStatisticsUseCase {
        return GetCollectionStatisticsUseCase(repository)
    }

    // Category use cases

    @Singleton
    @Provides
    fun provideGetCategoriesUseCase(repository: CategoryRepository): GetCategoriesUseCase {
        return GetCategoriesUseCase(repository)
    }

    // Museum use cases

    @Singleton
    @Provides
    fun provideGetMuseumArtifactsUseCase(repository: MuseumRepository): GetMuseumArtifactsUseCase {
        return GetMuseumArtifactsUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetSimilarMuseumArtifactsUseCase(repository: MuseumRepository): GetSimilarMuseumArtifactsUseCase {
        return GetSimilarMuseumArtifactsUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideAntiqueUseCases(
        getAntiqueUseCase: GetAntiqueByIdUseCase,
        getAntiquesUseCase: GetAntiquesUseCase,
        getAntiquesByCategoryUseCase: GetAntiquesByCategoryUseCase,
        addAntiqueUseCase: AddAntiqueUseCase,
        updateAntiqueUseCase: UpdateAntiqueUseCase,
        deleteAntiqueUseCase: DeleteAntiqueUseCase,
        searchAntiquesUseCase: SearchAntiquesUseCase,
        getCollectionStatisticsUseCase: GetCollectionStatisticsUseCase
    ): AntiqueUseCases {
        return AntiqueUseCases(
            getAntiqueUseCase,
            getAntiquesUseCase,
            getAntiquesByCategoryUseCase,
            addAntiqueUseCase,
            updateAntiqueUseCase,
            deleteAntiqueUseCase,
            searchAntiquesUseCase,
            getCollectionStatisticsUseCase
        )
    }

    @Singleton
    @Provides
    fun provideCategoryUseCases(
        getCategoriesUseCase: GetCategoriesUseCase
    ): CategoryUseCases {
        return CategoryUseCases(getCategoriesUseCase)
    }

    @Singleton
    @Provides
    fun provideMuseumUseCases(
        getMuseumArtifactsUseCase: GetMuseumArtifactsUseCase,
        getSimilarMuseumArtifactsUseCase: GetSimilarMuseumArtifactsUseCase
    ): MuseumUseCases {
        return MuseumUseCases(
            getMuseumArtifactsUseCase,
            getSimilarMuseumArtifactsUseCase
        )
    }
}
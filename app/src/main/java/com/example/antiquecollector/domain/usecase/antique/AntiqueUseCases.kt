package com.example.antiquecollector.domain.usecase.antique

import com.example.antiquecollector.domain.usecase.GetCollectionStatisticsUseCase

data class AntiqueUseCases(
    val getAntiqueUseCase: GetAntiqueByIdUseCase,
    val getAntiquesUseCase: GetAntiquesUseCase,
    val getAntiquesByCategoryUseCase: GetAntiquesByCategoryUseCase,
    val addAntiqueUseCase: AddAntiqueUseCase,
    val updateAntiqueUseCase: UpdateAntiqueUseCase,
    val deleteAntiqueUseCase: DeleteAntiqueUseCase,
    val searchAntiquesUseCase: SearchAntiquesUseCase,
    val getCollectionStatisticsUseCase: GetCollectionStatisticsUseCase
)
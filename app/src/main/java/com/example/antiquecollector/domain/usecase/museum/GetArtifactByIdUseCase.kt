package com.example.antiquecollector.domain.usecase.museum

import com.example.antiquecollector.domain.model.MuseumArtifact
import com.example.antiquecollector.domain.repository.MuseumRepository
import javax.inject.Inject

class GetArtifactByIdUseCase @Inject constructor(
    private val repository: MuseumRepository
) {
    /**
     * get museum artifact by Id
     *
     * @param id The Museum artifact Id
     * @return Single museum artifact matching the id
     */
    suspend operator fun invoke(id: String): MuseumArtifact? {
        if (id.isEmpty()) {
            return null
        }

        return repository.getArtifactById(id)
    }
}
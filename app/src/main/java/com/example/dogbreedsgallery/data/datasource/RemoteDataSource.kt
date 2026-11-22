
package com.example.dogbreedsgallery.data.datasource

import com.example.dogbreedsgallery.data.remote.BreedDto
import com.example.dogbreedsgallery.data.remote.BreedImageResponseDto
import com.example.dogbreedsgallery.data.remote.DogApiService

class RemoteDataSource(
    private val dogApiService: DogApiService
) {

    suspend fun getBreeds(): List<BreedDto> {
        return dogApiService.getBreeds()
    }

    suspend fun getBreedImages(
        breedId: Int,
        limit: Int = 20
    ): List<BreedImageResponseDto> {
        return dogApiService.getBreedImages(
            breedId = breedId,
            limit = limit
        )
    }
}

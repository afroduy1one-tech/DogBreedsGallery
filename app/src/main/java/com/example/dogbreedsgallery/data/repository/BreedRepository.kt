package com.example.dogbreedsgallery.data.repository

import com.example.dogbreedsgallery.data.local.BreedDao
import com.example.dogbreedsgallery.data.local.BreedEntity
import com.example.dogbreedsgallery.data.remote.BreedImageResponseDto
import com.example.dogbreedsgallery.data.remote.DogApiService
import kotlinx.coroutines.flow.Flow

class BreedRepository(
    private val breedDao: BreedDao,
    private val dogApiService: DogApiService
) {

    fun getAllBreeds(): Flow<List<BreedEntity>> {
        return breedDao.getAllBreeds()
    }

    fun getFavoriteBreeds(): Flow<List<BreedEntity>> {
        return breedDao.getFavoriteBreeds()
    }

    fun getBreedById(breedId: Int): Flow<BreedEntity?> {
        return breedDao.getBreedById(breedId)
    }

    suspend fun refreshBreeds() {
        val remoteBreeds = dogApiService.getBreeds()

        val favoriteIds = breedDao.getFavoriteBreedIds().toSet()
        val localBreeds = remoteBreeds.map { breed ->
            BreedEntity(
                id = breed.id,
                name = breed.name,
                bredFor = breed.bredFor,
                breedGroup = breed.breedGroup,
                lifeSpan = breed.lifeSpan,
                temperament = breed.temperament,
                origin = breed.origin,
                weightMetric = breed.weight?.metric,
                heightMetric = breed.height?.metric,
                imageUrl = breed.image?.url,
                isFavorite = breed.id in favoriteIds
            )
        }

        breedDao.insertBreeds(localBreeds)
    }

    suspend fun getBreedImages(breedId: Int): List<BreedImageResponseDto> {
        return dogApiService.getBreedImages(
            breedId = breedId,
            limit = 20
        )
    }

    suspend fun updateFavoriteStatus(
        breedId: Int,
        isFavorite: Boolean
    ) {
        breedDao.updateFavoriteStatus(breedId, isFavorite)
    }
}
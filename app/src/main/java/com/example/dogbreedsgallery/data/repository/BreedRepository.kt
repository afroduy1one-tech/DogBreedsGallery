
package com.example.dogbreedsgallery.data.repository

import com.example.dogbreedsgallery.data.datasource.LocalDataSource
import com.example.dogbreedsgallery.data.datasource.RemoteDataSource
import com.example.dogbreedsgallery.data.local.BreedEntity
import com.example.dogbreedsgallery.data.remote.BreedImageResponseDto
import kotlinx.coroutines.flow.Flow

class BreedRepository(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) {

    fun getAllBreeds(): Flow<List<BreedEntity>> {
        return localDataSource.getAllBreeds()
    }

    fun getFavoriteBreeds(): Flow<List<BreedEntity>> {
        return localDataSource.getFavoriteBreeds()
    }

    fun getBreedById(breedId: Int): Flow<BreedEntity?> {
        return localDataSource.getBreedById(breedId)
    }

    suspend fun refreshBreeds() {
        val remoteBreeds = remoteDataSource.getBreeds()

        val favoriteIds = localDataSource
            .getFavoriteBreedIds()
            .toSet()

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

        localDataSource.insertBreeds(localBreeds)
    }

    suspend fun getBreedImages(
        breedId: Int
    ): List<BreedImageResponseDto> {
        return remoteDataSource.getBreedImages(
            breedId = breedId,
            limit = 20
        )
    }

    suspend fun updateFavoriteStatus(
        breedId: Int,
        isFavorite: Boolean
    ) {
        localDataSource.updateFavoriteStatus(
            breedId = breedId,
            isFavorite = isFavorite
        )
    }
}

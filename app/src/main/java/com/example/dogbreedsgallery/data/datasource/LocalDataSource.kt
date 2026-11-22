
package com.example.dogbreedsgallery.data.datasource

import com.example.dogbreedsgallery.data.local.BreedDao
import com.example.dogbreedsgallery.data.local.BreedEntity
import kotlinx.coroutines.flow.Flow

class LocalDataSource(
    private val breedDao: BreedDao
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

    suspend fun insertBreeds(breeds: List<BreedEntity>) {
        breedDao.insertBreeds(breeds)
    }

    suspend fun updateFavoriteStatus(
        breedId: Int,
        isFavorite: Boolean
    ) {
        breedDao.updateFavoriteStatus(breedId, isFavorite)
    }

    suspend fun getFavoriteBreedIds(): List<Int> {
        return breedDao.getFavoriteBreedIds()
    }
}

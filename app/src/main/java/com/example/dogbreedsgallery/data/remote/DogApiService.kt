package com.example.dogbreedsgallery.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface DogApiService {

    @GET("breeds")
    suspend fun getBreeds(): List<BreedDto>

    @GET("images/search")
    suspend fun getBreedImages(
        @Query("breed_ids") breedId: Int,
        @Query("limit") limit: Int = 20
    ): List<BreedImageResponseDto>
}

data class BreedImageResponseDto(
    val id: String,
    val url: String,
    val width: Int?,
    val height: Int?
)
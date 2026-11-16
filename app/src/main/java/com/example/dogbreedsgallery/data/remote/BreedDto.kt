package com.example.dogbreedsgallery.data.remote

import com.squareup.moshi.Json

data class BreedDto(
    val id: Int,
    val name: String,
    @param:Json(name = "bred_for")
    val bredFor: String?,
    @param:Json(name = "breed_group")
    val breedGroup: String?,
    @param:Json(name = "life_span")
    val lifeSpan: String?,
    val temperament: String?,
    val origin: String?,
    val weight: MeasurementDto?,
    val height: MeasurementDto?,
    val image: BreedImageDto?
)

data class MeasurementDto(
    val imperial: String?,
    val metric: String?
)

data class BreedImageDto(
    val id: String?,
    val url: String?,
    val width: Int?,
    val height: Int?
)
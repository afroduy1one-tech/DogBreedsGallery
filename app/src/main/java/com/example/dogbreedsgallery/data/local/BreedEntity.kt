package com.example.dogbreedsgallery.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "breeds")
data class BreedEntity(
    @PrimaryKey
    val id: Int,

    val name: String,
    val bredFor: String?,
    val breedGroup: String?,
    val lifeSpan: String?,
    val temperament: String?,
    val origin: String?,
    val weightMetric: String?,
    val heightMetric: String?,
    val imageUrl: String?,

    val isFavorite: Boolean = false
)
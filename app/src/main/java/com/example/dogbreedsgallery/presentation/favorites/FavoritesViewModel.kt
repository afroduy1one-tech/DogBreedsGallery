package com.example.dogbreedsgallery.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogbreedsgallery.data.local.BreedEntity
import com.example.dogbreedsgallery.data.repository.BreedRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val repository: BreedRepository
) : ViewModel() {

    val favoriteBreeds: StateFlow<List<BreedEntity>> =
        repository.getFavoriteBreeds()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun removeFavorite(breed: BreedEntity) {
        viewModelScope.launch {
            repository.updateFavoriteStatus(
                breedId = breed.id,
                isFavorite = false
            )
        }
    }
}
package com.example.dogbreedsgallery.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dogbreedsgallery.data.repository.BreedRepository

class FavoritesViewModelFactory(
    private val repository: BreedRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            return FavoritesViewModel(repository) as T
        }

        throw IllegalArgumentException("Неизвестный класс ViewModel")
    }
}
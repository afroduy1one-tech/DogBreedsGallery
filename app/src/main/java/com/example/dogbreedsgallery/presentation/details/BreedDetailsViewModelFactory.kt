package com.example.dogbreedsgallery.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dogbreedsgallery.data.repository.BreedRepository

class BreedDetailsViewModelFactory(
    private val repository: BreedRepository,
    private val breedId: Int
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BreedDetailsViewModel::class.java)) {
            return BreedDetailsViewModel(
                repository = repository,
                breedId = breedId
            ) as T
        }

        throw IllegalArgumentException("Неизвестный класс ViewModel")
    }
}
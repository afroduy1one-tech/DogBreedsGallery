package com.example.dogbreedsgallery.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dogbreedsgallery.data.repository.BreedRepository

class HomeViewModelFactory(
    private val repository: BreedRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repository) as T
        }

        throw IllegalArgumentException(
            "Неизвестный класс ViewModel: ${modelClass.name}"
        )
    }
}

package com.example.dogbreedsgallery.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogbreedsgallery.data.local.BreedEntity
import com.example.dogbreedsgallery.data.repository.BreedRepository
import com.example.dogbreedsgallery.data.remote.BreedImageResponseDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BreedDetailsViewModel(
    private val repository: BreedRepository,
    private val breedId: Int
) : ViewModel() {

    private val _breed = MutableStateFlow<BreedEntity?>(null)
    val breed: StateFlow<BreedEntity?> = _breed.asStateFlow()

    private val _images =
        MutableStateFlow<List<BreedImageResponseDto>>(emptyList())
    val images: StateFlow<List<BreedImageResponseDto>> = _images.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        observeBreed()
        loadImages()
    }

    private fun observeBreed() {
        viewModelScope.launch {
            repository.getBreedById(breedId).collect { result ->
                _breed.value = result
            }
        }
    }

    private fun loadImages() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                _images.value = repository.getBreedImages(breedId)
            } catch (exception: Exception) {
                _error.value = "Не удалось загрузить фотографии"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleFavorite() {
        val currentBreed = _breed.value ?: return

        viewModelScope.launch {
            repository.updateFavoriteStatus(
                breedId = currentBreed.id,
                isFavorite = !currentBreed.isFavorite
            )
        }
    }
}

package com.example.dogbreedsgallery.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogbreedsgallery.data.local.BreedEntity
import com.example.dogbreedsgallery.data.repository.BreedRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: BreedRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _uiEvents = MutableSharedFlow<String>()
    val uiEvents: SharedFlow<String> = _uiEvents.asSharedFlow()

    val breeds: StateFlow<List<BreedEntity>> =
        combine(
            repository.getAllBreeds(),
            searchQuery
        ) { breeds, query ->
            if (query.isBlank()) {
                breeds
            } else {
                breeds.filter { breed ->
                    breed.name.contains(query, ignoreCase = true)
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    init {
        refreshBreeds()
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun refreshBreeds() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                repository.refreshBreeds()
            } catch (exception: Exception) {
                val message = exception.message ?: "Не удалось загрузить породы"
                _error.value = message

                if (breeds.value.isNotEmpty()) {
                    _uiEvents.emit(message)
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleFavorite(breed: BreedEntity) {
        viewModelScope.launch {
            repository.updateFavoriteStatus(
                breedId = breed.id,
                isFavorite = !breed.isFavorite
            )
        }
    }
}
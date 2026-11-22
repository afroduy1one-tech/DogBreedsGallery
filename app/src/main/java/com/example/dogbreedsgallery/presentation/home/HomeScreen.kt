package com.example.dogbreedsgallery.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dogbreedsgallery.data.repository.BreedRepository

@Composable
fun HomeScreen(
    repository: BreedRepository,
    onBreedClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val homeViewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(repository)
    )

    val breeds by homeViewModel.breeds.collectAsState()
    val isLoading by homeViewModel.isLoading.collectAsState()
    val error by homeViewModel.error.collectAsState()
    val searchQuery by homeViewModel.searchQuery.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = homeViewModel::onSearchQueryChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Поиск породы") },
            placeholder = { Text("Например, Labrador") },
            singleLine = true
        )

        when {
            isLoading && breeds.isEmpty() -> {
                CircularProgressIndicator()
            }

            error != null && breeds.isEmpty() -> {
                Text(error ?: "Ошибка загрузки")
            }

            breeds.isEmpty() -> {
                Text("Породы собак не найдены")
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = breeds,
                        key = { it.id }
                    ) { breed ->
                        BreedCard(
                            breed = breed,
                            onFavoriteClick = {
                                homeViewModel.toggleFavorite(breed)
                            },
                            onClick = {
                                onBreedClick(breed.id)
                            }
                        )
                    }
                }
            }
        }
    }
}
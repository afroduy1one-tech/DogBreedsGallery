package com.example.dogbreedsgallery.presentation.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dogbreedsgallery.data.repository.BreedRepository
import com.example.dogbreedsgallery.presentation.home.BreedCard

@Composable
fun FavoritesScreen(
    repository: BreedRepository,
    modifier: Modifier = Modifier
) {
    val favoritesViewModel: FavoritesViewModel = viewModel(
        factory = FavoritesViewModelFactory(repository)
    )

    val favoriteBreeds by favoritesViewModel.favoriteBreeds.collectAsState()

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = "Избранное",
            modifier = Modifier.padding(16.dp)
        )

        if (favoriteBreeds.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Здесь пока нет избранных пород")
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = favoriteBreeds,
                    key = { it.id }
                ) { breed ->
                    BreedCard(
                        breed = breed,
                        onFavoriteClick = {
                            favoritesViewModel.removeFavorite(breed)
                        }
                    )
                }
            }
        }
    }
}
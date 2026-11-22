
package com.example.dogbreedsgallery.presentation.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.dogbreedsgallery.data.repository.BreedRepository

@Composable
fun BreedDetailsScreen(
    repository: BreedRepository,
    breedId: Int,
    modifier: Modifier = Modifier
) {
    val detailsViewModel: BreedDetailsViewModel = viewModel(
        key = "breed_details_$breedId",
        factory = BreedDetailsViewModelFactory(
            repository = repository,
            breedId = breedId
        )
    )

    val breed by detailsViewModel.breed.collectAsState()
    val images by detailsViewModel.images.collectAsState()
    val isLoading by detailsViewModel.isLoading.collectAsState()
    val error by detailsViewModel.error.collectAsState()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            when {
                images.isNotEmpty() -> {
                    val pagerState = rememberPagerState(
                        pageCount = { images.size }
                    )

                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                    ) { page ->
                        AsyncImage(
                            model = images[page].url,
                            contentDescription = "Фотография породы",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Text(
                        text = "Фото ${pagerState.currentPage + 1} из ${images.size}",
                        modifier = Modifier.padding(top = 8.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                isLoading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                error != null -> {
                    Text(error ?: "Не удалось загрузить фотографии")
                }
            }
        }

        item {
            when {
                breed == null -> {
                    Text("Информация о породе загружается...")
                }

                else -> {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val currentBreed = breed!!

                        Text(
                            text = currentBreed.name,
                            style = MaterialTheme.typography.headlineMedium
                        )

                        IconButton(
                            onClick = detailsViewModel::toggleFavorite
                        ) {
                            Icon(
                                imageVector = if (currentBreed.isFavorite) {
                                    Icons.Filled.Favorite
                                } else {
                                    Icons.Outlined.FavoriteBorder
                                },
                                contentDescription = if (currentBreed.isFavorite) {
                                    "Удалить из избранного"
                                } else {
                                    "Добавить в избранное"
                                },
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        DetailItem("Происхождение", currentBreed.origin)
                        DetailItem("Группа", currentBreed.breedGroup)
                        DetailItem("Для чего выведена", currentBreed.bredFor)
                        DetailItem("Продолжительность жизни", currentBreed.lifeSpan)
                        DetailItem("Темперамент", currentBreed.temperament)
                        DetailItem("Вес", currentBreed.weightMetric?.let { "$it кг" })
                        DetailItem("Рост", currentBreed.heightMetric?.let { "$it см" })
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailItem(
    title: String,
    value: String?
) {
    if (!value.isNullOrBlank()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall
            )

            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

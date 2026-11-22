
package com.example.dogbreedsgallery.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(homeViewModel) {
        homeViewModel.uiEvents.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->

        PullToRefreshBox(
            isRefreshing = isLoading,
            onRefresh = homeViewModel::refreshBreeds,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = homeViewModel::onSearchQueryChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        label = {
                            Text("Поиск породы")
                        },
                        placeholder = {
                            Text("Например, Labrador")
                        },
                        singleLine = true
                    )
                }

                when {
                    isLoading && breeds.isEmpty() -> {
                        item {
                            CircularProgressIndicator(
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }

                    error != null && breeds.isEmpty() -> {
                        item {
                            Text(
                                text = error ?: "Ошибка загрузки",
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }

                    breeds.isEmpty() -> {
                        item {
                            Text(
                                text = "Породы собак не найдены",
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }

                    else -> {
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
}

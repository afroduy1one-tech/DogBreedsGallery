package com.example.dogbreedsgallery.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.dogbreedsgallery.data.repository.BreedRepository
import com.example.dogbreedsgallery.presentation.favorites.FavoritesScreen
import com.example.dogbreedsgallery.presentation.home.HomeScreen


@Composable
fun AppNavigation(repository: BreedRepository) {
    val backStack = remember {
        mutableStateListOf<NavKey>(HomeRoute)
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = backStack.lastOrNull() == HomeRoute,
                    onClick = {
                        backStack.clear()
                        backStack.add(HomeRoute)
                    },
                    icon = {},
                    label = { Text("Каталог") }
                )

                NavigationBarItem(
                    selected = backStack.lastOrNull() == FavoritesRoute,
                    onClick = {
                        backStack.clear()
                        backStack.add(HomeRoute)
                        backStack.add(FavoritesRoute)
                    },
                    icon = {},
                    label = { Text("Избранное") }
                )
            }
        }
    ) { innerPadding ->
        NavDisplay(
            backStack = backStack,
            onBack = {
                if (backStack.size > 1) {
                    backStack.removeAt(backStack.lastIndex)
                }
            },
            entryProvider = entryProvider {
                addEntryProvider(HomeRoute) {
                    HomeScreen(
                        repository = repository,
                        onBreedClick = { breedId: Int ->
                            backStack.add(DetailsRoute(breedId))
                        }
                    )
                }

                addEntryProvider(FavoritesRoute) {
                    FavoritesScreen(repository = repository)
                }

                addEntryProvider(DetailsRoute::class) { key ->
                    Text("Детали породы: ${key.breedId}")
                }
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}
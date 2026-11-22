package com.example.dogbreedsgallery.presentation.navigation

import androidx.navigation3.runtime.NavKey

data object HomeRoute : NavKey

data object FavoritesRoute : NavKey

data class DetailsRoute(val breedId: Int) : NavKey
package com.example.dogbreedsgallery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dogbreedsgallery.presentation.home.HomeViewModel
import com.example.dogbreedsgallery.presentation.home.HomeViewModelFactory
import com.example.dogbreedsgallery.ui.theme.DogBreedsGalleryTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as DogBreedsApplication

        setContent {
            DogBreedsGalleryTheme {

                val homeViewModel: HomeViewModel = viewModel(
                    factory = HomeViewModelFactory(
                        app.dependencies.breedRepository
                    )
                )

                val breeds by homeViewModel.breeds.collectAsState()
                val isLoading by homeViewModel.isLoading.collectAsState()
                val error by homeViewModel.error.collectAsState()

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        when {
                            isLoading && breeds.isEmpty() -> {
                                CircularProgressIndicator()
                            }

                            error != null && breeds.isEmpty() -> {
                                Text(text = error ?: "Ошибка загрузки")
                            }

                            breeds.isEmpty() -> {
                                Text(text = "Породы собак не найдены")
                            }

                            else -> {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    items(
                                        items = breeds,
                                        key = { breed -> breed.id }
                                    ) { breed ->
                                        Text(
                                            text = breed.name,
                                            modifier = Modifier.padding(8.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}